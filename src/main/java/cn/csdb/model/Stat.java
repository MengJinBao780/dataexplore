package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by huangwei on 2018/4/16.
 */
//统计结果
@Document(collection = "stat")
public class Stat {
    @Id
    private String id;
    @Field("userNum")
    private String userNum;
    @Field("dataSize")
    private String dataSize;
    @Field("fileNum")
    private String fileNum;
    @Field("visitNum")
    private String visitNum;
    @Field("datasetNum")
    private String datasetNum;
    @Field("downloadNum")
    private String downloadNum;
    @Field("stattime")
    private Date stattime;

    public Date getStattime() {
        return stattime;
    }

    public void setStattime(Date stattime) {
        this.stattime = stattime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getDataSize() {
        return dataSize;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public String getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(String visitNum) {
        this.visitNum = visitNum;
    }

    public String getDatasetNum() {
        return datasetNum;
    }

    public void setDatasetNum(String datasetNum) {
        this.datasetNum = datasetNum;
    }

    public String getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(String downloadNum) {
        this.downloadNum = downloadNum;
    }
}
