package cn.csdb.repository;


import cn.csdb.model.SdoRelation;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by ajian on 2018/5/15.
 */
@Repository
public class SdoRelationDao {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取该资源的关联信息
     *
     * @param sdoId
     * @return
     */
    public List<SdoRelation> getBySdoId(String sdoId) {
        DBObject query = QueryBuilder.start()
                .and("sdo_id")
                .is(sdoId)
                .get();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", false);//不需要获取该字段，默认需要
        BasicQuery basicQuery = new BasicQuery(query, basicDBObject);
        return mongoTemplate.find(basicQuery, SdoRelation.class);

    }

    /**
     * 若主键已存存在，则进行更新操作
     *
     * @param sdoRelation
     */
    public void save(SdoRelation sdoRelation) {
        mongoTemplate.save(sdoRelation);
    }

    public void updateRelate(SdoRelation sdoRelation) {
        DBObject query = QueryBuilder.start()
                .and("sdo_id")
                .is(sdoRelation.getSdoId())
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        Update stat = Update.update("sdo_relates", sdoRelation.getSdoRelates());
        mongoTemplate.updateFirst(basicQuery, stat, SdoRelation.class);
    }

    public Date getMaxLastCompleteCreateTime() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(1);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "last_create_time");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoRelation> sdoRelations = mongoTemplate.find(basicQuery, SdoRelation.class);
        if (sdoRelations.size() == 0) {
            return null;
        }
        return sdoRelations.get(0).getLastCreateTime();
    }

    public Date getMaxCreateTime() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(1);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "create_time");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoRelation> sdoRelations = mongoTemplate.find(basicQuery, SdoRelation.class);
        if (sdoRelations.size() == 0) {
            return null;
        }
        return sdoRelations.get(0).getCreateTime();
    }

    public long getCount() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.count(basicQuery, SdoRelation.class);
    }

    public List<SdoRelation> getByPage(long offset, long curSize) {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip((int) offset);
        basicQuery.limit((int) curSize);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "create_time");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        return mongoTemplate.find(basicQuery, SdoRelation.class);
    }

    public Date getMinCreateTime() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(1);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "create_time");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoRelation> sdoRelations = mongoTemplate.find(basicQuery, SdoRelation.class);
        if (sdoRelations.size() == 0) {
            return null;
        }
        return sdoRelations.get(0).getCreateTime();
    }
}
