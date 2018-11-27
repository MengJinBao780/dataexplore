package cn.csdb.controller.fileview;

import cn.csdb.model.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
public class StrategyController {
    public boolean viewData(FileInfo fileInfo, HttpServletResponse response) throws IOException {
        String filePath = System.getProperty("dataExplore.root");
        File file = new File(filePath,fileInfo.getPreviewFile());
        if (!file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024 * 2];
        int len;
        OutputStream outputStream = response.getOutputStream();
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
        return true;
    }
}
