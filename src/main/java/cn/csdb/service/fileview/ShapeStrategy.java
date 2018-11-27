package cn.csdb.service.fileview;

import cn.csdb.model.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ShapeStrategy extends Strategy{
    @Override
    public String handleView(FileInfo fileInfo, Model model) {
        model.addAttribute("fileId", fileInfo.getId());
        model.addAttribute("fileName", fileInfo.getFileName());
        return "/fileview/shape";
    }
}
