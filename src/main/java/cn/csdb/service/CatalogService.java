package cn.csdb.service;

import cn.csdb.model.Catalog;
import cn.csdb.repository.CatalogDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CatalogService {
    @Resource
    private CatalogDao catalogDao;

    public Map<String,String> getCatalogName(String id){
        Map<String,String> map = new HashMap<>();
        Catalog catalog = catalogDao.getCatalogById(id);
        if (catalog ==null){
            map.put("catalog","");
            return map;
        }
        map.put("catalog",catalog.getcName());
        return map;
    }
}
