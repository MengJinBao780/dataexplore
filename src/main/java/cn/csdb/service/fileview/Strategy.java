package cn.csdb.service.fileview;

import cn.csdb.model.FileInfo;
import org.springframework.ui.Model;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
public abstract class Strategy {

    public abstract String handleView(FileInfo fileInfo, Model model);

    public boolean handleData(FileInfo fileInfo, String contentType) {
        return true;
    }


}
