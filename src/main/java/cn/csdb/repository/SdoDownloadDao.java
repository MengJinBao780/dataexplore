package cn.csdb.repository;

import cn.csdb.model.SdoDownload;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class SdoDownloadDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //添加下载日志
    public void addLog(String loginId,String fileId,String fileName,String sdoId,String title){
        SdoDownload sdoDownload = new SdoDownload();
        sdoDownload.setSdoId(sdoId);
        sdoDownload.setLoginId(loginId);
        sdoDownload.setFileId(fileId);
        sdoDownload.setFileName(fileName);
        sdoDownload.setCreateTime(DateUtils.addHours(new Date(),8));
        sdoDownload.setTitle(title);
        mongoTemplate.save(sdoDownload);
    }

    //分页数据
    public List<SdoDownload> getList(String loginid, int start, int pageSize){
        DBObject query = QueryBuilder.start()
                .and("loginId")
                .is(loginid)
                .and("fileId").is("")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(start);
        basicQuery.limit(pageSize);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoDownload> list = mongoTemplate.find(basicQuery, SdoDownload.class);
        System.out.println(list.size());
        return list;
    }

    //统计用户收藏的记录条数
    public long getCount(String loginId){
        return mongoTemplate.count(new Query(Criteria.where("loginId").is(loginId).and("fileId").is("")),SdoDownload.class);
    }

    public List<SdoDownload> getBy(String title,String loginId,Date beginTime,Date endTime){
        QueryBuilder queryBuilder = QueryBuilder.start();
        if(title !=null && !title.equals("")){
            queryBuilder = queryBuilder.and("title").regex(Pattern.compile("^.*"+title+".*$"));
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
        return mongoTemplate.find(basicQuery,SdoDownload.class);
    }

    public long getTotalCount(){
        return mongoTemplate.count(new Query(),SdoDownload.class);
    }
}
