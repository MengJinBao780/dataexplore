package cn.csdb.repository;

import cn.csdb.model.Catalog;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CatalogDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //根据id查询catalog
    public Catalog getCatalogById(String id){
        return mongoTemplate.findById(id,Catalog.class);
    }
}
