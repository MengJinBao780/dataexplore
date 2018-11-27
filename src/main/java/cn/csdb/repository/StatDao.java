package cn.csdb.repository;

import cn.csdb.model.SdoFavorites;
import cn.csdb.model.Stat;
import cn.csdb.model.User;
import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by pirate on 2018/4/16.
 */
@Repository
public class StatDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void createCollection() {
        DBCollection stat = mongoTemplate.createCollection("stat");
    }

    public void saveStat(Stat stat) {
        mongoTemplate.save(stat);
    }

    public Stat getLastStat(){
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(1);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "stattime");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        Stat stat = mongoTemplate.findOne(basicQuery, Stat.class);
        return stat;
    }


    public void addData(String userNum,String datasetNum,String dataSize,String downloadNum,String fileNum,String visitNum){
        Stat stat = new Stat();
        stat.setUserNum(userNum);
        stat.setDatasetNum(datasetNum);
        stat.setDataSize(dataSize);
        stat.setDownloadNum(downloadNum);
        stat.setFileNum(fileNum);
        stat.setVisitNum(visitNum);
        stat.setStattime(DateUtils.addHours(new Date(),8));
        mongoTemplate.save(stat);
    }

}
