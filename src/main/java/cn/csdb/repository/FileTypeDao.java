package cn.csdb.repository;

import cn.csdb.model.FileType;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FileTypeDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //根据sdo的id获取一条fileType
    public FileType getFileTypeBySdoId(String id){
        DBObject query = QueryBuilder.start().and("sdo_id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.findOne(basicQuery,FileType.class);
    }

    public FileType getById(String id){
        return mongoTemplate.findById(id,FileType.class);
    }
}
