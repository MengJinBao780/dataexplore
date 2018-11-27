package cn.csdb.repository;

import cn.csdb.model.Tag;
import cn.csdb.model.TagDetail;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class TagDetailDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //添加用户自定义标签，等待审核
    public void addTag(String prodId,String sdoId,String tagName,String loginId){
        TagDetail tagDetail = new TagDetail();
        tagDetail.setProdId(prodId);
        tagDetail.setSdoId(sdoId);
        tagDetail.setTagName(tagName);
        tagDetail.setLoginId(loginId);
        tagDetail.setStatus(0);
        tagDetail.setAuditDate(DateUtils.addHours(new Date(),8));
        tagDetail.setAuditor("");
        mongoTemplate.save(tagDetail);
    }

    //根据id删除标签数据
    public void delTag(String id){
        DBObject query = QueryBuilder.start().and("_id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        mongoTemplate.remove(basicQuery,TagDetail.class);
    }

    //查询用户对某数据定义的未审核的一个标签
    public List<TagDetail> selTag(String loginId, String sdoId, String tagName){
        DBObject query = QueryBuilder.start().and("login_id").is(loginId).and("sdo_id").is(sdoId).and("tag_name").is(tagName).and("stat").is(0).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.find(basicQuery,TagDetail.class);
    }

    //查询用户对某数据定义的所有未审核标签
    public List<TagDetail> selTag(String loginId, String sdoId){
        DBObject query = QueryBuilder.start().and("login_id").is(loginId).and("sdo_id").is(sdoId).and("stat").is(0).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.find(basicQuery,TagDetail.class);
    }

    //根据id查询标签
    public TagDetail getTagDetail(String id){
        DBObject query = QueryBuilder.start().and("id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.findById(basicQuery,TagDetail.class);
    }

    public List<TagDetail> getTagsByName(String tagName,String sdoId) {
        DBObject query = QueryBuilder.start().and("tag_name").is(tagName).and("sdo_id").is(sdoId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "number");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(20);
        List<TagDetail> tagDetails = mongoTemplate.find(basicQuery, TagDetail.class);
        return tagDetails;
    }


}
