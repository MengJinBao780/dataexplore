package cn.csdb.repository;

import cn.csdb.model.ContentEdit;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mysql.jdbc.Statement;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sophie on 2017/11/8.
 */
@Repository
public class ContentEditDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public List<ContentEdit> getListByPage(String colType,int start, int pageSize){
        DBObject query = QueryBuilder.start()
                .and("ctName")
                .is(colType)
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(start);
        basicQuery.limit(pageSize);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<ContentEdit> contentEdits = mongoTemplate.find(basicQuery, ContentEdit.class);
        System.out.println(contentEdits.size());
        return contentEdits;
    }

    public long countByPage(String colType)
    {

        return mongoTemplate.count(new Query(Criteria.where("ctName").is(colType)), ContentEdit.class);
    }

    public int deleteCollectionByColId(String id)
    {
        mongoTemplate.remove(mongoTemplate.findById(id, ContentEdit.class));
        return 1;
    }

    public ContentEdit getCollectionByColId(String id){

        ContentEdit contentEdit= mongoTemplate.findById(id, ContentEdit.class);
        return contentEdit;
    }


    public int editCollection(ContentEdit contentEdit,String id) {
             mongoTemplate.findAndModify(new Query(Criteria.where("id").is(id)),
                new Update().set("loginId", contentEdit.getLoginId()).set("author", contentEdit.getAuthor()).set("title", contentEdit.getTitle()).set("ctName", contentEdit.getCtName()).set("content",contentEdit.getContent()),
                ContentEdit.class);
        return 1;
    }


    public int addCollection(final ContentEdit contentEdit) {

        contentEdit.setCtName(contentEdit.getCtName());
        contentEdit.setStatus(1);
        contentEdit.setAuthor(contentEdit.getAuthor());
        contentEdit.setLoginId(contentEdit.getLoginId());
        contentEdit.setCreateTime(contentEdit.getCreateTime());
        contentEdit.setTitle(contentEdit.getTitle());
        mongoTemplate.save(contentEdit);
        return 1;

    }

    //byCql
    //获取最新的5个新闻
    public List<ContentEdit> getTop5NewsListByField() {
        DBObject query = QueryBuilder.start()
                .and("ct_name")
                .is("新闻")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(5);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "_id");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));

        List<ContentEdit> contentEdits = mongoTemplate.find(basicQuery, ContentEdit.class);
        return contentEdits;
    }

//
//    public Collection getCollectionByTitleAndTime(String colTitle, String colTime) {
//        String sqlSB = "SELECT tc.*,tcc.content as content FROM t_collection tc,t_colcontent tcc where " +
//                "  tc.colConId=tcc.colConId and colTitle=? and colTime =? ";
//        return jdbcTemplate.queryForObject(sqlSB, new CollectionMapper(), colTitle,colTime);
//    }
//
//    //获取最新的5个新闻
//    public List<Collection> getTop5NewsList(String colType){
//        StringBuilder sb = new StringBuilder("SELECT * FROM t_collection WHERE colType=? order by colTime DESC limit 0,5");
//        return jdbcTemplate.query(sb.toString(), new Object[]{colType},new CollectionMapper());
//    }
//
//
//    //获取某一个栏目的所有记录内容
//    public List<Collection> getListByColType(String colType){
//        StringBuilder sb = new StringBuilder("SELECT * FROM t_collection WHERE colType=? order by sort ");
//        return jdbcTemplate.query(sb.toString(), new Object[]{colType},new CollectionMapper());
//    }

}
