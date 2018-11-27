package cn.csdb.db;

import cn.csdb.model.FileInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreadTest extends Thread {
    private List<List<String>> data ;
    private MongoTemplate mongoTemplate;
    public ThreadTest(List<List<String>> data,MongoTemplate mongoTemplate){
        this.data=data;
        this.mongoTemplate=mongoTemplate;
    }

    @Override
    public void run(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i=0;i<data.size();i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("256");
            fileInfo.setFtId("5b2c6e5d9ef7101a4078470a");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("#77");
            fileInfo.setCreateTime(DateUtils.addHours(now,8));
            fileInfo.setUpdateTime(DateUtils.addHours(now,8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            try {
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)),8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)),8));
            }catch (Exception e){
                e.printStackTrace();
            }
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082df");
            mongoTemplate.save(fileInfo);
        }

    }
}
