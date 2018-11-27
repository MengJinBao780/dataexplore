package cn.csdb.repository;

import cn.csdb.model.ContentEditType;
import cn.csdb.model.Sdo;
import cn.csdb.model.Tag;
import cn.csdb.model.Product;
import cn.csdb.model.TagDetail;
import cn.csdb.model.TagMapping;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class TagDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public List<Tag> getTagsByProd(String prodId) {
        DBObject query = QueryBuilder.start().and("prod_id").is(prodId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "number");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(20);
        List<Tag> tags = mongoTemplate.find(basicQuery, Tag.class);
        return tags;
    }

    public List<Tag> getTop40Tags() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "number");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(40);
        List<Tag> tags = mongoTemplate.find(basicQuery, Tag.class);
        return tags;
    }

    public TagDetail addTagDetail(TagDetail tagDetail) {
        mongoTemplate.save(tagDetail);
        return tagDetail;
    }

    public List<Tag> getTagsByName(String tagName) {
        DBObject query = QueryBuilder.start().and("tag_name").is(tagName).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "number");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(20);
        List<Tag> tags = mongoTemplate.find(basicQuery, Tag.class);
        return tags;
    }

    public List<TagDetail> getTagList(int start, int pageSize, int type, String sdoName, String prodId, int status) {

        DBObject query = null;
        if (type == 1) {
            query = QueryBuilder.start().and("status")
                    .notEquals(0).get();
            if(status==1||status==-1)
            {
                query = QueryBuilder.start().and("status").is(status).get();
        }
        }
        if (type == 0) {
            query = QueryBuilder.start().and("status")
                    .is(0).get();
        }
        BasicQuery basicQuery = new BasicQuery(query);
        if (!prodId.equals("all")) {
            basicQuery.addCriteria(Criteria.where("prodId").is(prodId));
        }
        if (sdoName !=null && !sdoName.equals("")) {
            basicQuery.addCriteria(Criteria.where("sdoTitle").regex(Pattern.compile("^.*"+sdoName+".*$")));
        }
        basicQuery.skip(start);
        basicQuery.limit(pageSize);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "auditDate");
        List<Sort.Order> sos = Lists.newArrayList(so);
        basicQuery.with(new Sort(sos));
        Sort.Order so1 = new Sort.Order(Sort.Direction.DESC, "sdoId");
        List<Sort.Order> sos1 = Lists.newArrayList(so1);
        basicQuery.with(new Sort(sos1));
        List<TagDetail> tags = mongoTemplate.find(basicQuery, TagDetail.class);
        for (int i = 0; i < tags.size(); i++) {
            TagDetail tagDetail = tags.get(i);
            String proName = getProName(tagDetail.getId());
            tagDetail.setProdName(proName);
//            String sdoTitle = getSdoTitle(tagDetail.getId());
//            tagDetail.setSdoTitle(sdoTitle);
        }
        return tags;
    }

    public long countByPage(int type) {
        long a = 0;
        if (type == 1) {
            a = mongoTemplate.count(new Query(Criteria.where("status").nin(0)), TagDetail.class);
        }
        if (type == 0) {
            a = mongoTemplate.count(new Query(Criteria.where("status").is(type)), TagDetail.class);
        }
        return a;
    }

    public boolean approveTag(String id, String auditor) {
        Date date = new Date();
        TagDetail tagDetail = mongoTemplate.findAndModify(new Query(Criteria.where("id").is(id)),
                new Update().set("auditDate", DateUtils.addHours(date,8)).set("auditor", auditor).set("status", 1),
                TagDetail.class);
//        判断sdo中有无相应的标签
        Sdo sdo = mongoTemplate.findById(tagDetail.getSdoId(), Sdo.class);
        List<String> tags = sdo.getTags();
        if (!tags.contains(tagDetail.getTagName())) {
//        如果没有，则更新sdo，添加tags
            Query query = Query.query(Criteria.where("id").is(tagDetail.getSdoId()));
            Update update = new Update().push("tags", tagDetail.getTagName());
            mongoTemplate.updateFirst(query, update, Sdo.class);
        }
        //更新Tag表，增加引用次数，或者新建一个正式标签。
        Tag tag = mongoTemplate.findOne(Query.query(Criteria.where("tagName").is(tagDetail.getTagName())), Tag.class);
        if (tag == null) {
            tag = new Tag();
            tag.setNumber(1);
            tag.setProdId(tagDetail.getProdId());
            tag.setTagName(tagDetail.getTagName());
            mongoTemplate.save(tag);
            //更新sdomapping,加入一条映射。
            TagMapping tagMapping = new TagMapping();
            tagMapping.setSdoId(tagDetail.getId());
            tagMapping.setTagId(tagDetail.getId());
            mongoTemplate.save(tagMapping);
        } else {
            mongoTemplate.findAndModify(Query.query(Criteria.where("tagName").is(tagDetail.getTagName())), new Update().set("number", tag.getNumber() + 1),
                    Tag.class);
        }

        return true;
    }


    public boolean rejectTag(String id, String auditor) {
        Date date = new Date();
        mongoTemplate.findAndModify(new Query(Criteria.where("id").is(id)),
                new Update().set("auditDate", DateUtils.addHours(date,8)).set("auditor", auditor).set("status", -1),
                TagDetail.class);
        return true;
    }

    public String getSdoTitle(String id) {
        TagDetail tagDetail = mongoTemplate.findById(id, TagDetail.class);
        Sdo sdo = mongoTemplate.findById(tagDetail.getSdoId(), Sdo.class);
        return sdo.getTitle();
    }

    public String getProName(String id) {
        TagDetail tagDetail = mongoTemplate.findById(id, TagDetail.class);
        Product product = mongoTemplate.findById(tagDetail.getProdId(), Product.class);
        return product.getProdName();
    }

    public Tag findByProductIdAndTagName(String prodId, String tagName)
    {
        DBObject query = QueryBuilder.start().and("prodId").is(prodId).and("tagName").is(tagName).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Tag> tags = mongoTemplate.find(basicQuery, Tag.class);
        if (tags.size() > 0){
            return tags.get(0);
        }else
            return null;
    }

}