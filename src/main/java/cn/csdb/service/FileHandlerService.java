package cn.csdb.service;

import cn.csdb.model.FileInfo;
import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.ReadOp;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rados.jna.RadosObjectInfo;
import com.google.common.collect.Queues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by ajian on 2018/5/8.
 */
@Service
public class FileHandlerService {

    private Logger logger = LoggerFactory.getLogger(FileHandlerService.class);
    private final ExecutorService readExecutorService = Executors.newFixedThreadPool(8);
    private final ExecutorService writeExecutorService = Executors.newFixedThreadPool(8);
    private final static int BYTE_SIZE = 1 * 1024 * 1024;
    private final static int QUEUE_SIZE = 4;
    private final static int QUEUE_TIMEOUT = 10 * 60 * 1000;
    private final static String CEPH_MON_HOST = "10.0.138.80";
    private final static String CEPH_KEY = "AQDIYLtaTLajERAAp9QoMRH2K2dcRej5pIAbMQ==";
    private final static String CEPH_POOL = "testpool";

    public void upload() {

    }

    /**
     * 文件下载
     *
     * @param response
     * @param fileId
     */
    public void download(HttpServletResponse response, String fileId) {
        BlockingQueue<byte[]> byteArrays = Queues.newLinkedBlockingQueue(QUEUE_SIZE);//阻塞queue缓存数据
        FileObjectReadTask fileObjectTask = new FileObjectReadTask(fileId, byteArrays);//生成ceph读取文件数据任务
        Future<String> future = readExecutorService.submit(fileObjectTask);
        boolean fileSizeFlag = true;
        while ((!fileObjectTask.isFinish()) && (!future.isDone())) {//循环寸queue读取数据
            writeBytesToResponse(response, byteArrays, fileObjectTask, fileSizeFlag);
            fileSizeFlag = false;
        }
        while (byteArrays.size() > 0) {
            writeBytesToResponse(response, byteArrays, fileObjectTask, fileSizeFlag);
            fileSizeFlag = false;
        }

    }


    /**
     * 写入数据到response
     *
     * @param response
     * @param byteArrays
     */
    private void writeBytesToResponse(HttpServletResponse response, BlockingQueue<byte[]> byteArrays,
                                      FileObjectReadTask fileObjectTask, boolean fileSizeFlag) {
        try {
            byte[] bytes = byteArrays.poll(QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
            if (bytes == null) {
                return;
            }
            if (fileSizeFlag) {
                long fileSize = fileObjectTask.getFileSize();
                response.setContentLengthLong(fileSize);//设置下载文件length
            }
            response.getOutputStream().write(bytes);//写入数据到response 输出流
        } catch (InterruptedException e) {
            logger.error("file load error:", e);
        } catch (IOException e) {
            logger.error("file read error:", e);

        }
    }


    /**
     * 文件打包zip下载
     *
     * @param fileInfos
     * @param response
     * @throws IOException
     * @throws InterruptedException
     */
    public void downloadZipFiles(List<FileInfo> fileInfos, HttpServletResponse response) throws IOException, InterruptedException {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(response.getOutputStream());//打包zip文件输出流，写入到response
            for (FileInfo fileInfo : fileInfos) {//循环遍历文件
                compressFile(fileInfo, zipOutputStream);
            }
        } finally {
            if (zipOutputStream != null) {
                zipOutputStream.flush();
                zipOutputStream.close();
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
        }
    }

    /**
     * 读取一个文件数据，并添加到压缩包
     *
     * @param fileInfo
     * @param zipOut
     * @throws IOException
     * @throws InterruptedException
     */
    private void compressFile(FileInfo fileInfo, ZipOutputStream zipOut) throws IOException, InterruptedException {
        ZipEntry entry = new ZipEntry(fileInfo.getFileName());
        zipOut.putNextEntry(entry);
        BlockingQueue<byte[]> byteArrays = Queues.newLinkedBlockingQueue(QUEUE_SIZE);
        FileObjectReadTask fileObjectTask = new FileObjectReadTask(fileInfo.getId(), byteArrays);
        Future<String> future = readExecutorService.submit(fileObjectTask);
        while ((!fileObjectTask.isFinish()) && (!future.isDone())) {
            byte[] bytes = byteArrays.poll(QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
            if (bytes != null) {
                zipOut.write(bytes);
            }
        }
        while (byteArrays.size() > 0) {
            byte[] bytes = byteArrays.poll(QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
            if (bytes != null) {
                zipOut.write(bytes);
            }
        }
    }

    /**
     * 文件上传保存到ceph
     *
     * @param inputStream
     * @param fileId
     * @throws IOException
     * @throws InterruptedException
     */
    public void saveFile(InputStream inputStream, String fileId) throws IOException, InterruptedException {
        BlockingQueue<byte[]> byteArrays = Queues.newLinkedBlockingQueue(QUEUE_SIZE);
        FileObjectWriteTask fileObjectWriteTask = null;
        try {
            fileObjectWriteTask = new FileObjectWriteTask(fileId, byteArrays);
            writeExecutorService.submit(fileObjectWriteTask);
            byte[] bytes = new byte[BYTE_SIZE];
            int read = inputStream.read(bytes);
            while ((read > -1)) {
                if (fileObjectWriteTask.isFinish()) {
                    throw new IOException();
                }
                if (read < BYTE_SIZE) {
                    bytes = Arrays.copyOfRange(bytes, 0, read);
                }
                boolean offer = byteArrays.offer(bytes, QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
                if (!offer) {
                    break;
                }
                bytes = new byte[BYTE_SIZE];
                read = inputStream.read(bytes);
            }
        } finally {
            if (fileObjectWriteTask != null) {
                fileObjectWriteTask.setFinish();
            }
            inputStream.close();
        }
    }

    class FileObjectWriteTask extends FileObjectTask {

        public FileObjectWriteTask(String fileId, BlockingQueue<byte[]> byteArrays) {
            super(fileId, byteArrays);
        }

        public String call() {
            String result = "ok";
            try {
                initCephPool();
                while ((!isFinish())) {
                    byte[] bytes = byteArrays.poll(QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
                    if (bytes != null) {
                        pool.append(fileId, bytes);
                    } else {
                        break;
                    }
                }
                while (byteArrays.size() > 0) {
                    pool.append(fileId, byteArrays.take());
                }
            } catch (Exception e) {
                logger.error("rados file write error: fileId:" + fileId, e);
                result = "fail";
            } finally {
                setFinish();
                closeCephPool();
            }
            return result;
        }

    }

    class FileObjectReadTask extends FileObjectTask {
        protected volatile long fileSize = 0;

        public FileObjectReadTask(String fileId, BlockingQueue<byte[]> byteArrays) {
            super(fileId, byteArrays);
        }

        @Override
        public String call() {
            String result = "ok";
            try {
                initCephPool();
                RadosObjectInfo info = pool.stat(fileId);
                long objectSize = info.getSize();//获取文件大小
                fileSize = objectSize;
                ReadOp readOp = pool.readOpCreate();
                executeRead(readOp, objectSize);//读取文件实体数据
            } catch (Exception e) {
                logger.error("rados file read error: fileId:" + fileId, e);
                result = "fail";
            } finally {
                setFinish();
                closeCephPool();
            }
            return result;
        }

        /**
         * 执行数据读取操作
         *
         * @param readOp
         * @param objectSize
         * @throws RadosException
         */
        private void executeRead(ReadOp readOp, long objectSize) throws RadosException, InterruptedException {
            long offset = 0;
            while (offset < objectSize) {
                ReadOp.ReadResult readResult;
                if ((offset + BYTE_SIZE) <= objectSize) {
                    readResult = readOp.queueRead(offset, BYTE_SIZE);
                } else {
                    readResult = readOp.queueRead(offset, objectSize - offset);
                }
                readOp.operate(fileId, Rados.OPERATION_NOFLAG);
                long bytesRead = readResult.getBytesRead();
                byte[] bytes = new byte[(int) bytesRead];
                readResult.getBuffer().get(bytes);
                boolean offer = byteArrays.offer(bytes, QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
                if (!offer) {
                    break;
                }
                offset += BYTE_SIZE;
            }
        }

        protected long getFileSize() {
            return fileSize;
        }

    }

    abstract class FileObjectTask implements Callable<String> {

        protected String fileId;
        protected BlockingQueue<byte[]> byteArrays;

        private volatile boolean isFinish;

        protected Rados rados;
        protected IoCTX pool;

        public FileObjectTask(String fileId, BlockingQueue<byte[]> byteArrays) {
            this.fileId = fileId;
            this.byteArrays = byteArrays;
            isFinish = false;
        }

        /**
         * 创建连接
         *
         * @throws RadosException
         */
        protected void initCephPool() throws RadosException {
            rados = new Rados("admin");
            rados.confSet("mon_host", CEPH_MON_HOST);
            rados.confSet("key", CEPH_KEY);
            rados.connect();
            pool = rados.ioCtxCreate(CEPH_POOL);
        }

        /**
         * 关闭连接
         */
        protected void closeCephPool() {
            if (pool != null) {
                try {
                    pool.close();
                } catch (IOException e) {
                    logger.error("rados pool close error: ", e);
                }
            }
            if (rados != null) {
                rados.shutDown();
            }
        }


        protected boolean isFinish() {
            return isFinish;
        }

        protected void setFinish() {
            isFinish = true;
        }
    }


}
