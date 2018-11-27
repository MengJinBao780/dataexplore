package cn.csdb.service;

import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.ReadOp;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rados.jna.RadosObjectInfo;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


/**
 * Created by ajian on 2018/4/24.
 */
public class CephTest {
    private final static String CEPH_MON_HOST = "10.0.138.80";
    //private final static String CEPH_KEY = "AQDIYLtaTLajERAAp9QoMRH2K2dcRej5pIAbMQ==";
    private final static String CEPH_KEY = "AQDxMT9bC+/bGhAAdefTDoPojZ/vjFlfhOPkIg==";
    private final static String CEPH_POOL = "data";


    @Test
    public void test1() throws IOException {
        CephTest cephTest = new CephTest();
        try {
            cephTest.initCephPool();
            cephTest.writeObject();
            cephTest.readObject();
            cephTest.objectSize();
        } finally {
            cephTest.closeCephPool();
        }

    }

    private void objectSize() throws RadosException {
        String fileId = "5accb6609ef71c18dc32f91b";
        RadosObjectInfo info = pool.stat(fileId);
        System.out.println("hello.123文件大小：" + info.getSize());
    }

    private void readObject() throws RadosException {
        String fileId = "5accb6609ef71c18dc32f91b";
        ReadOp readOp = pool.readOpCreate();
        ReadOp.ReadResult readResult = readOp.queueRead(0, 100);
        readOp.operate(fileId, Rados.OPERATION_NOFLAG);
        System.out.println(readResult.getBytesRead());
        byte[] bytes = new byte[100];
        readResult.getBuffer().get(bytes);
        System.out.println("文件内容:" + new String(bytes));

    }

    private void writeObject() throws IOException {
        String fileId = "5accb6609ef71c18dc32f91b";
        pool.write(fileId, Files.toByteArray(new File("/root/x.pdf")));
    }

    private void poolList() throws RadosException {
        String[] strings = rados.poolList();
        System.out.println("池列表：");
        for (String str : strings) {
            System.out.println(str);
        }
    }

    private void ObjectList() throws RadosException {
//        pool.remove("hello.1234");//删除某个object
        String[] listObjects = pool.listObjects();
        for (String str : listObjects) {
            System.out.println(str);
        }
    }


    private Rados rados;
    private IoCTX pool;

    /**
     * 创建连接
     *
     * @throws RadosException
     */
    private void initCephPool() throws RadosException {
        rados = new Rados("admin");
        rados.confSet("mon_host", CEPH_MON_HOST);
        rados.confSet("key", CEPH_KEY);
        rados.connect();
        System.out.println("connect ok!-------------");
        pool = rados.ioCtxCreate(CEPH_POOL);
    }

    /**
     * 关闭连接
     */
    private void closeCephPool() {
        if (pool != null) {
            try {
                pool.close();
            } catch (IOException e) {
            }
        }
        if (rados != null) {
            rados.shutDown();
        }
    }

}
