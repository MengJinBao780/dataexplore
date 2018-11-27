package cn.csdb.service;

import cn.csdb.model.FileType;
import cn.csdb.repository.FileTypeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileTypeService {
    @Resource
    private FileTypeDao fileTypeDao;


    public Map<String,String> getFtName(String id){
        Map<String,String> map = new HashMap<>();
        FileType fileType = fileTypeDao.getFileTypeBySdoId(id);
        if (fileType==null){
            map.put("ftName","");
            return map;
        }
        map.put("ftName",fileType.getFtName());
        return map;
    }

    public FileType getById(String id){
        return fileTypeDao.getById(id);
    }

    public FileType getBySdoId(String sdoId){
        return fileTypeDao.getFileTypeBySdoId(sdoId);
    }
}
