package cn.csdb.controller.fileview;

import cn.csdb.model.FileInfo;
import cn.csdb.service.FileInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ShapeController extends StrategyController {


    @Resource
    private FileInfoService fileInfoService;

    @RequestMapping("/sdo/file/view/shape/data")
    public void viewVideo(@RequestParam(name = "fileId") String fileId, HttpServletResponse response) throws IOException {
        FileInfo fileInfo = fileInfoService.getById(fileId);
        viewData(fileInfo, response);
    }
}
