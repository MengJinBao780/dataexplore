package cn.csdb.service;

import cn.csdb.model.FileTemplate;
import cn.csdb.repository.FileTemplateDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileTemplateService {
    @Resource
    private FileTemplateDao fileTemplateDao;

    //根据文件类型查出所有查询字段
    public List<Map<String,String>> findSearchField(String fileType){
        List<FileTemplate> list = fileTemplateDao.findSearchField(fileType);
        List<Map<String,String>> maps = new ArrayList<>();
        if (list==null || list.size()==0){
            return maps;
        }
        for (int i=0;i<list.size();i++){
            Map<String,String> map = new HashMap<>();
            map.put("fieldName",list.get(i).getFieldName());
            map.put("fieldTitle",list.get(i).getFieldTitle());
            map.put("fieldType",list.get(i).getFieldType());
            maps.add(map);
        }
        return maps;
    }

    //根据文件类型查出所有显示字段
    public List<Map<String,String>> findShowField(String fileType){
        List<FileTemplate> list = fileTemplateDao.findShowField(fileType);
        List<Map<String,String>> maps = new ArrayList<>();
        if (list==null || list.size()==0){
            return maps;
        }
        for (int i=0;i<list.size();i++){
            Map<String,String> map = new HashMap<>();
            map.put("fieldName",list.get(i).getFieldName());
            map.put("fieldTitle",list.get(i).getFieldTitle());
            maps.add(map);
        }
        return maps;
    }
}
