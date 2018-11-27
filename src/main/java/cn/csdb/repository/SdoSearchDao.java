package cn.csdb.repository;

import cn.csdb.model.SdoSearch;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class SdoSearchDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public void save(String loginId, String keyword){
        SdoSearch sdoSearch = new SdoSearch();
        sdoSearch.setLoginId(loginId);
        sdoSearch.setKeyword(keyword);
        sdoSearch.setCreateTime(DateUtils.addHours(new Date(),8));
        mongoTemplate.save(sdoSearch);
    }

    //收索最多的6个关键字
    public AggregationResults<BasicDBObject> getSearchWord(){
        TypedAggregation<SdoSearch> agg = Aggregation.newAggregation(SdoSearch.class,
                //Aggregation.project("keyword"),
                Aggregation.group("keyword").count().as("count"),
                Aggregation.sort(Sort.Direction.DESC,"count"),
                Aggregation.project("keyword").and("searchWord").previousOperation(),
                Aggregation.limit(6));
        return mongoTemplate.aggregate(agg,BasicDBObject.class);

    }

    public List<SdoSearch> getAll(){
        return mongoTemplate.findAll(SdoSearch.class);
    }

    //条件查询
    public List<SdoSearch> getBy(String keyword ,String loginId,Date beginTime,Date endTime){
        QueryBuilder queryBuilder = QueryBuilder.start();
        if(keyword !=null && !keyword.equals("")){
            queryBuilder = queryBuilder.and("keyword").regex(Pattern.compile("^.*"+keyword+".*$"));
        }
        if (loginId !=null && !loginId.equals("")){
            queryBuilder = queryBuilder.and("login_id").regex(Pattern.compile("^.*"+loginId+".*$"));
        }
        if (beginTime !=null){
            queryBuilder =queryBuilder.and("create_time").greaterThanEquals(beginTime);
        }
        if (endTime != null){
            queryBuilder = queryBuilder.and("create_time").lessThanEquals(endTime);
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "create_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery,SdoSearch.class);
    }
}
