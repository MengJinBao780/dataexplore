package cn.csdb.repository;

import cn.csdb.model.FileInfo;
import cn.csdb.model.SdoVisit;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class SdoVisitDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //浏览记录
    public void addLog(String loginId, String sdoId, String title) {
        SdoVisit sdoVisit = new SdoVisit();
        sdoVisit.setSdoId(sdoId);
        sdoVisit.setLoginId(loginId);
        sdoVisit.setCreateTime(DateUtils.addHours(new Date(),8));
        sdoVisit.setTitle(title);
        mongoTemplate.save(sdoVisit);
    }

    public List<SdoVisit> getAll() {
        return mongoTemplate.findAll(SdoVisit.class);
    }

    public List<SdoVisit> getBy(String title, String loginId, Date beginTime, Date endTime) {
        QueryBuilder queryBuilder = QueryBuilder.start();
        if (title != null && !title.equals("")) {
            queryBuilder = queryBuilder.and("title").regex(Pattern.compile("^.*" + title + ".*$"));
        }
        if (loginId != null && !loginId.equals("")) {
            queryBuilder = queryBuilder.and("login_id").regex(Pattern.compile("^.*" + loginId + ".*$"));
        }
        if (beginTime != null) {
            queryBuilder = queryBuilder.and("create_time").greaterThanEquals(beginTime);
        }
        if (endTime != null) {
            queryBuilder = queryBuilder.and("create_time").lessThanEquals(endTime);
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "create_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery, SdoVisit.class);
    }

    public void update(SdoVisit visit) {
        mongoTemplate.save(visit);
    }

    public List getAllLoginId() {
        return mongoTemplate.getCollection("sdo_visit").distinct("login_id");
    }

    public List<SdoVisit> getByloginId(String loginId) {
        QueryBuilder queryBuilder = QueryBuilder.start().and("login_id").is(loginId);
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "create_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery, SdoVisit.class);
    }

    public List<SdoVisit> getSdoVisits(String loginId, Date createTime) {
        QueryBuilder queryBuilder = QueryBuilder.start().and("login_id").is(loginId).and("create_time").greaterThan(createTime);
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "create_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery, SdoVisit.class);
    }

    public long countSdoVisits(){
        long l = mongoTemplate.count(new Query(),SdoVisit.class);
        return l;
    }
}
