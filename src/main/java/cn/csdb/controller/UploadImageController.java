package cn.csdb.controller;
import cn.csdb.model.UploadResponse;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sophie on 2018/4/27.
 */

/**
 * UEditor文件上传辅助类
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadImageController {
    private Logger logger = LoggerFactory.getLogger(UploadImageController.class);

    @Resource
    private ServletContext servletContext;
    @Value("#{systemPro['collectImagePath']}")
    private String collectImagePath = "/resources/collectImage/resourceImage/";
    HashMap<String, String> states = new HashMap<>();

    @PostConstruct
    public void init() {
        states.put("SUCCESS", "SUCCESS"); //默认成功
        states.put("NOFILE", "未包含文件上传域");
        states.put("TYPE", "不允许的文件格式");
        states.put("SIZE", "文件大小超出限制");
        states.put("ENTYPE", "请求类型ENTYPE错误");
        states.put("REQUEST", "上传请求异常");
        states.put("IO", "IO异常");
        states.put("DIR", "目录创建失败");
        states.put("UNKNOWN", "未知错误");
    }

    @RequestMapping(value = "image", method = RequestMethod.POST)
    @ResponseBody
    public UploadResponse upload(@RequestParam(value = "upfile") MultipartFile imageFile) {
        String fileName = null;
        String fileType = null;
        try {
            String resourceImageFilePath = WebUtils.getRealPath(servletContext, collectImagePath);
            File dir = new File(resourceImageFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //String[] fileTypes = { ".rar", ".doc", ".docx", ".zip", ".pdf",".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
            String[] fileTypes = {"gif", "png", "jpg", "jpeg", "bmp"};
            logger.debug(imageFile.getOriginalFilename());
            String[] splits = imageFile.getOriginalFilename().toLowerCase().split("\\.");
            if (splits == null || splits.length == 0 || !Lists.newArrayList(fileTypes).contains(splits[splits.length - 1])) {
                return new UploadResponse(null, 0, null, null, states.get("TYPE"));
            }
            fileType = splits[splits.length - 1];
            fileName = System.currentTimeMillis() + "" + RandomUtils.nextInt(10000, 99999) + "." + fileType;
            File file = new File(dir.getAbsolutePath() + File.separator + fileName);
            imageFile.transferTo(file);
        } catch (IOException e) {
            logger.error("upload image error:", e);
            return new UploadResponse(null, 0, null, null, states.get("IO"));
        }
        return new UploadResponse(fileName, imageFile.getSize(), fileType, collectImagePath + "/"+fileName, states.get("SUCCESS"));
    }


}