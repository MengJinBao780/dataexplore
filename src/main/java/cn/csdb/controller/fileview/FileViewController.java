package cn.csdb.controller.fileview;

import cn.csdb.model.FileInfo;
import cn.csdb.service.FileInfoService;
import cn.csdb.service.fileview.Strategy;
import cn.csdb.service.fileview.StrategyFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Controller
public class FileViewController {
    @Resource
    private StrategyFactory strategyFactory;
    @Resource
    private FileInfoService fileInfoService;

    @RequestMapping("/sdo/file/view")
    public String resView(
            @RequestParam(name = "fileId") String fileId, Model model) {
        /**
         * 根据fileid获取资源的描述信息，如：展示插件类型、文件名称等
         */
        FileInfo fileInfo = fileInfoService.getById(fileId);
        Strategy strategy = strategyFactory.getStrategy(fileInfo.getPreviewType());
        if (strategy == null) {
            return "error";
        }
        return strategy.handleView(fileInfo, model);
    }
    @RequestMapping("/sdo/office")
    public String toOfficePage(@RequestParam("filePath")String filePath,String fileType,Model model){
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(filePath);
        Strategy strategy = strategyFactory.getStrategy(fileType);
        if (strategy == null) {
            return "error";
        }
        return strategy.handleView(fileInfo, model);
    }
    @RequestMapping("/sdo/readExcel")
    public String readExcel(String filePath,String fileType,Model model){
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(filePath);
        Strategy strategy = strategyFactory.getStrategy(fileType);
        if (strategy == null) {
            return "error";
        }
        return strategy.handleView(fileInfo, model);
    }
}
