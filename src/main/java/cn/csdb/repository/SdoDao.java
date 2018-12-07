package cn.csdb.repository;

import cn.csdb.model.FileInfo;
import cn.csdb.model.Sdo;
import cn.csdb.model.SdoFavorites;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Repository
public class SdoDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //获取所有的sdo记录列表
    public List<Sdo> getAll() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "publisher_publishTime");
        List<Sort.Order> orders = Lists.newArrayList(order);
        basicQuery.with(new Sort(orders));
        List<Sdo> list = mongoTemplate.find(basicQuery, Sdo.class);
        return list;
    }

    //排序并获取前15条sdo集合
    public List<cn.csdb.model.Resource> getInitDates(String order) {
        DBObject query = QueryBuilder.start().get();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", true);
        basicDBObject.put("title", true);
        basicDBObject.put("imagePath", true);
        basicDBObject.put("createTime", true);
        basicDBObject.put("publishOrgnization", true);
/*
        basicDBObject.put("vCount",true);
*/
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, order);
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        BasicQuery basicQuery = new BasicQuery(query, basicDBObject);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(15);
        List<cn.csdb.model.Resource> sdos = mongoTemplate.find(basicQuery, cn.csdb.model.Resource.class);
        return sdos;
    }

    //根据id获取sdo对象
    public cn.csdb.model.Resource getSdoById(String id) {
        return mongoTemplate.findById(id, cn.csdb.model.Resource.class);
    }

    public Sdo getSdoByPid(String pid) {
        return mongoTemplate.findById(pid, Sdo.class);
    }

    //sdo浏览次数+1
    public void addVisitCount(String id) {
        cn.csdb.model.Resource sdo = getSdoById(id);
        DBObject query = QueryBuilder.start().and("_id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        if(sdo.getvCount()==null){
            sdo.setvCount(1);
        }
        Update count = Update.update("vCount", sdo.getvCount() + 1);
        mongoTemplate.updateFirst(basicQuery, count, cn.csdb.model.Resource.class);
    }

    //sdo下载次数+1
    public void addDownloadCount(String id) {
        cn.csdb.model.Resource sdo = getSdoById(id);
        DBObject query = QueryBuilder.start().and("_id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        if(sdo.getdCount()==null){
            sdo.setdCount(0);
        }
        Update count = Update.update("dCount", sdo.getdCount() + 1);
        mongoTemplate.updateFirst(basicQuery, count, cn.csdb.model.Resource.class);
    }

    //获取sdo对像中不同的文件类型
    public List<String> findDistinctDataType() {
        List<String> list = mongoTemplate.getCollection("sdo")
                .distinct("dataFormat");
        return list;
    }


    //获取sdo对像中不同的数据发布者
    public List<String> findDistinctPublisher() {
        List<String> list = mongoTemplate.getCollection("sdo")
                .distinct("publisher.name");
        return list;
    }

    //获取指定sdoid 列表记录中的tags
    public List<Map<String, String>> getMapsBySdoList(List<Sdo> sdoList) {
        List<Map<String, String>> list = new ArrayList<>();
        for (Sdo sdo : sdoList) {
            for (String str : sdo.getTags()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("prodId", sdo.getProductId());
                map.put("sdoId", sdo.getId());
                map.put("tagName", str);
                list.add(map);
            }
        }
        return list;
    }

    public Date getMaxCreateTime() {
        Sdo sdo = getMaxCreateTimeSdo();
        return sdo == null ? null : sdo.getCreateTime();
    }

    public Sdo getMaxCreateTimeSdo() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(1);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "create_time");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<Sdo> sdoList = mongoTemplate.find(basicQuery, Sdo.class);
        if (sdoList.size() == 0) {
            return null;
        }
        return sdoList.get(0);
    }

    public long getCountAfterCreateTime(Date createTime) {
        QueryBuilder queryBuilder = QueryBuilder.start();
        if (createTime != null) {
            queryBuilder.and("create_time").greaterThan(createTime);
        }
        DBObject query = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.count(basicQuery, Sdo.class);
    }

    public List<Sdo> getSdoList(Date maxCreateTime, long offset, long curSize) {
        QueryBuilder queryBuilder = QueryBuilder.start();
        if (maxCreateTime != null) {
            queryBuilder.and("create_time").greaterThan(maxCreateTime);
        }
        DBObject query = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip((int) offset);
        basicQuery.limit((int) curSize);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "create_time");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        return mongoTemplate.find(basicQuery, Sdo.class);
    }


    public long getCount() {
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.count(basicQuery, Sdo.class);
    }

    public List<Sdo> getSdoList(long offset, long curSize) {
        return getSdoList(null, offset, curSize);
    }

    public List<Sdo> getSdoByIds(List<String> sdoList) {
        QueryBuilder queryBuilder = QueryBuilder.start();
        queryBuilder.and("_id").in(sdoList);
        DBObject query = queryBuilder.get();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("title", true);
//        basicDBObject.put("_id", true);
        BasicQuery basicQuery = new BasicQuery(query, basicDBObject);
        return mongoTemplate.find(basicQuery, Sdo.class);
    }


    public List<Sdo> getSdosByIds(List<String> sdoList) {
        QueryBuilder queryBuilder = QueryBuilder.start();
        queryBuilder.and("_id").in(sdoList);
        DBObject query = queryBuilder.get();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("desc", true);
        basicDBObject.put("publisher", true);
        basicDBObject.put("downloadCount", true);
        basicDBObject.put("toFilesNumber", true);
        basicDBObject.put("dataFormat", true);
        basicDBObject.put("_id", true);
        basicDBObject.put("icon_path", true);
        BasicQuery basicQuery = new BasicQuery(query, basicDBObject);
        return mongoTemplate.find(basicQuery, Sdo.class);
    }


    //获取时间范围
    public List<Date> getDateRange(){
        List<Date> dateRange = new ArrayList<>();
        DBObject dbObject = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.ASC, "publisher_publishTime");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(1);
        dateRange.add(mongoTemplate.find(basicQuery,Sdo.class).get(0).getPublisherPublishTime());
        so = new Sort.Order(Sort.Direction.DESC, "publisher_publishTime");
        sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(1);
        dateRange.add(mongoTemplate.find(basicQuery,Sdo.class).get(0).getPublisherPublishTime());
        return dateRange;
    }

    public long countSdos(){
        long l = mongoTemplate.count(new Query(),Sdo.class);
        return l;
    }

    public List<Sdo> getAllSdos(){
        return mongoTemplate.find(new Query(), Sdo.class);
    }


    //xiajl20180717 根据名称来获取SDO实体信息
    public Sdo getByTitle(String title){
        DBObject query = QueryBuilder.start().and("title").is(title).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Sdo> sdoList = mongoTemplate.find(basicQuery,Sdo.class);
        if(sdoList.size() == 0){
            return null;
        }else{
            return sdoList.get(0);
        }
    }
}