package cn.csdb.repository;

import cn.csdb.model.Sdo;
import cn.csdb.model.SdoComment;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sophie on 2018/4/24.
 */
@Repository
public class SdoCommentDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public void insertSdoComment(final SdoComment sdoComment) {
        sdoComment.setContent(sdoComment.getContent());
        sdoComment.setScore(sdoComment.getScore());
        sdoComment.setLoginId(sdoComment.getLoginId());
        sdoComment.setCreateTime(sdoComment.getCreateTime());
        sdoComment.setSdoId(sdoComment.getSdoId());
        sdoComment.setStatus(sdoComment.getStatus());
        mongoTemplate.save(sdoComment);
    }

    public List<SdoComment> getSdoCommentBySdoId(String sdoId) {
        return mongoTemplate.find(new Query(Criteria.where("sdoId").is(sdoId)), SdoComment.class);
    }

    public String getSdoIdById(String id){
        SdoComment sdoComment = mongoTemplate.findById(id,SdoComment.class);
        return sdoComment.getSdoId();
    }

    public long getSdoCommentNum(String sdoId) {
        return mongoTemplate.count(new Query(Criteria.where("sdoId").is(sdoId)), SdoComment.class);
    }

    public Boolean hasScored(String sdoId, String loginId) {
        long i = mongoTemplate.count(new Query(Criteria.where("sdoId").is(sdoId).and("loginId").is(loginId)), SdoComment.class);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<SdoComment> getSdoCommentList(String sdoId, int start, int pageSize) {
        DBObject query = QueryBuilder.start()
                .and("sdoId")
                .is(sdoId)
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(start);
        basicQuery.limit(pageSize);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoComment> sdoComments = mongoTemplate.find(basicQuery, SdoComment.class);
        System.out.println(sdoComments.size());
        return sdoComments;
    }

    public void updateAvgscore(String sdoId,double avgscore){
        mongoTemplate.findAndModify(new Query(Criteria.where("id").is(sdoId)),
                new Update().set("scores",avgscore),
                Sdo.class);
    }

    public double getAvgScore(String sdoId){

        Sdo sdo = mongoTemplate.findById(sdoId,Sdo.class);
        return sdo.getScores();
    }

    public boolean deleteScore(String id)
    {
        mongoTemplate.remove(mongoTemplate.findById(id, SdoComment.class));
        return true;
    }

//    public int getScoreNum(int datasetid) {
//        String sql = "select count(*) from f_score where datasetid=?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{datasetid}, Integer.TYPE);
//    }


//
//    public List<ScoreRemark> getAllScore(int sheetId) {
//        String sql = "select * from f_score where datasetid=?";
//        return jdbcTemplate.query(sql, new Object[]{sheetId}, new ScoreRemarkMapper());
//    }


}
