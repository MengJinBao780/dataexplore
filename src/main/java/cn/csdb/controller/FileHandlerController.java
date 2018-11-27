package cn.csdb.controller;

import cn.csdb.model.FileInfo;
import cn.csdb.model.UploadResponse;
import cn.csdb.service.FileHandlerService;
import cn.csdb.service.FileInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * Created by ajian on 2018/5/7.
 */
@Controller
public class FileHandlerController {

    private Logger logger = LoggerFactory.getLogger(FileHandlerController.class);

    @Resource
    private FileHandlerService fileHandlerService;

    @Resource
    private FileInfoService fileInfoService;


    @RequestMapping(value = "/file/uploadPage")
    public String uploadPage() {
        return "/uploadPage";
    }


    @RequestMapping(value = "/file/list")
    @ResponseBody
    public List<FileInfo> fileList() {
        int cephFlag = 1;
        return fileInfoService.getFiles(cephFlag);
    }


    @RequestMapping("/file/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,
                             String fileId) throws IOException {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        FileInfo fileInfo = fileInfoService.getById(fileId);
        if (fileInfo == null) {
            return;
        }
        String fileName = fileInfo.getFileName();
        response.setContentType("application/octet-stream");
        if (isMSBrowser(request)) {   //IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"),
                    "ISO-8859-1");
        }
        response.setHeader("Content-disposition",
                "attachment;filename=\"" + fileName + "\"");
        fileHandlerService.download(response, fileId);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    @RequestMapping("/file/download/zip")
    public void downloadZipFiles(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "fileIds[]") String[] fileIds, String zipName) throws IOException, InterruptedException {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        if (fileIds == null | fileIds.length == 0) {
            return;
        }
        List<FileInfo> fileInfos = fileInfoService.getByIds(fileIds);
        if (fileInfos == null || fileInfos.size() == 0) {
            return;
        }
        String fileName = "null";
        if (StringUtils.isNoneBlank(zipName)) {
            fileName = zipName;
        }
        fileName = fileName + ".zip";
        response.setContentType("application/octet-stream");
        if (isMSBrowser(request)) {   //IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"),
                    "ISO-8859-1");
        }
        response.setHeader("Content-disposition",
                "attachment;filename=\"" + fileName + "\"");
        fileHandlerService.downloadZipFiles(fileInfos, response);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    private static String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};

    public static boolean isMSBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal))
                return true;
        }
        return false;
    }


    @RequestMapping(value = "file/upload", method = RequestMethod.POST)
    @ResponseBody
    public UploadResponse upload(MultipartHttpServletRequest request) {
        try {
            List<MultipartFile> files = request.getFiles("files");
            if (files.isEmpty()) {
                return new UploadResponse(null, 0, null, null, "IO exception");
            }
            MultipartFile multipartFile = files.get(0);
            String fileName = multipartFile.getOriginalFilename();
            String fileId = fileInfoService.save(fileName);
            fileHandlerService.saveFile(multipartFile.getInputStream(), fileId);
            return new UploadResponse(fileName, multipartFile.getSize(), "", fileId, "SUCCESS");
        } catch (IOException e) {
            logger.error("upload file error:", e);
            return new UploadResponse(null, 0, null, null, "IO exception");
        } catch (InterruptedException e) {
            logger.error("upload file error:", e);
            return new UploadResponse(null, 0, null, null, "IO exception");
        }
    }


}
