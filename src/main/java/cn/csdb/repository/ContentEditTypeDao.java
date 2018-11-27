package cn.csdb.repository;

import cn.csdb.model.ContentEditType;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sophie on 2017/11/20.
 */
@Repository
public class ContentEditTypeDao {
    @Resource
    private MongoTemplate mongoTemplate;

//    public int add(Category category){
//        String sql="insert into t_category(name,sort) values(?,?) ";
//        return jdbcTemplate.update(sql, category.getName(), category.getSort());
//    }

    public int add(ContentEditType contentEditType) {
        contentEditType.setCtName(contentEditType.getCtName());
        contentEditType.setStatus(1);
        contentEditType.setOrder(contentEditType.getOrder());
        mongoTemplate.save(contentEditType);
        return 1;
    }

//    public int update(Category category){
//        String sql="update t_category set name=?, sort=? where id=?";
//        return jdbcTemplate.update(sql,category.getName(),category.getSort(),category.getId());
//    }

    public int update(ContentEditType contentEditType) {
        mongoTemplate.findAndModify(new Query(Criteria.where("id").is(contentEditType.getId())),
                new Update().set("ctName", contentEditType.getCtName()).set("order", contentEditType.getOrder()).set("status",1),
                ContentEditType.class);

        return 1;
    }


    public int delete(String id) {
        mongoTemplate.remove(mongoTemplate.findById(id, ContentEditType.class));
        return 1;
    }

//    public List<Category> getAll(){
//        String sql="select * from t_category order by sort ";
//        return jdbcTemplate.query(sql,new CategoryMapper());
//    }

    public List<ContentEditType> getAll() {

        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.ASC, "order");
        List<Sort.Order> sos = Lists.newArrayList(so);
        basicQuery.with(new Sort(sos));
        List<ContentEditType> contentEditTypeList = mongoTemplate.find(basicQuery,ContentEditType.class);
        for (ContentEditType contentEditType : contentEditTypeList) {
            System.out.println(contentEditType.getId());
            System.out.println(contentEditType.getCtName());

        }
        return contentEditTypeList;
    }
//
//    //获取新闻类的栏目
//    public List<Category> getAllNews() {
//        String sql = "select * from t_category where ISNEWS = 1 order by sort ";
//        return jdbcTemplate.query(sql, new CategoryMapper());
//    }


    public ContentEditType get(String id) {

        ContentEditType contentEditType= mongoTemplate.findById(id, ContentEditType.class);
        return contentEditType;
    }
}
