package cn.csdb.service;

import cn.csdb.model.SdoComment;
import cn.csdb.repository.SdoCommentDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sophie on 2018/4/24.
 */
@Service
public class SdoCommentService {

    @Resource
    private SdoCommentDao sdoCommentDao;

    public int insertSdoComment(final SdoComment sdoComment){
        sdoCommentDao.insertSdoComment(sdoComment);
        String sdoId = sdoComment.getSdoId();

        List<SdoComment> allScore = sdoCommentDao.getSdoCommentBySdoId(sdoId);
        double totalScore = 0.0;
        for (SdoComment sr : allScore) {
            double score = sr.getScore();
            totalScore += score;
        }
        BigDecimal bd = new BigDecimal(totalScore/allScore.size());
        double db = bd.doubleValue();
//      bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        updateAvgscore(sdoId,db);
        return 1;

    }

    public List<SdoComment> getSdoCommentsBySdoId(String SdoId){
        return sdoCommentDao.getSdoCommentBySdoId(SdoId);
    }

    public long getSdoCommentNum(String SdoId){
        return sdoCommentDao.getSdoCommentNum(SdoId);
    }

    public Boolean hasScored(String sdoId,String username){
        return sdoCommentDao.hasScored(sdoId,username);
    }

    public List<SdoComment> getSdoCommentList(String sdoId,int start,int pageSize){
        return sdoCommentDao.getSdoCommentList(sdoId,start,pageSize);
    }

    public void updateAvgscore(String sdoId,double avgscore){
        sdoCommentDao.updateAvgscore(sdoId,avgscore);
    }

    public double getAvgScore(String sdoId){
        return sdoCommentDao.getAvgScore(sdoId);
    }
    public boolean deleteScore(String id){

        String sdoId = sdoCommentDao.getSdoIdById(id);
        sdoCommentDao.deleteScore(id);
        List<SdoComment> allScore = sdoCommentDao.getSdoCommentBySdoId(sdoId);
        double totalScore = 0.0;
        for (SdoComment sr : allScore) {
            double score = sr.getScore();
            totalScore += score;
        }
        double db =0;
        if(allScore.size()>0) {
            BigDecimal bd = new BigDecimal(totalScore / allScore.size());
            db = bd.doubleValue();
        }
//      bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        updateAvgscore(sdoId,db);
        return true;
    }
}
