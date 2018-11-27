package cn.csdb.repository;

import cn.csdb.model.FileTemplate;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileTemplateDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //根据文件类型查找所有查询字段
    public List<FileTemplate> findSearchField(String fileType){
        DBObject query = QueryBuilder.start().and("file_type").is(fileType).and("is_search").is(1).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.ASC, "sort_order");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery,FileTemplate.class);
    }

    //根据文件类型查找所有显示字段
    public List<FileTemplate> findShowField(String fileType){
        DBObject query = QueryBuilder.start().and("file_type").is(fileType).and("is_show").is(1).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.ASC, "sort_order");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery,FileTemplate.class);
    }
}
