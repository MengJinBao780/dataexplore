package cn.csdb.statSchedule;

import cn.csdb.model.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;

/**
 * Created by pirate on 2018/4/13.
 */
public class StatTask {

    @Resource
    private MongoTemplate mongoTemplate;

    public static void main(String[] args){

    }

//    用户数
    public String getUserNum(){
        Long userNum = mongoTemplate.count(new Query(),User.class);
        return userNum.toString();
    }

//    数据量
    public String getDataSize(){
        return "";
    }

//    文件总数
    public String getFileNum(){
        Long userNum = mongoTemplate.count(new Query(),FileInfo.class);
        return userNum.toString();
    }

//    访问人次
    public String getVisitNum(){
        Long userNum = mongoTemplate.count(new Query(),VisitDataset.class);
        return userNum.toString();
    }

//    数据集数
    public String getDatasetNum(){
        Long userNum = mongoTemplate.count(new Query(),Sdo.class);
        return userNum.toString();
    }

//    下载量
    public String getDownloadNum(){
        Long userNum = mongoTemplate.count(new Query(),FileDownload.class);
        return userNum.toString();
    }

}
