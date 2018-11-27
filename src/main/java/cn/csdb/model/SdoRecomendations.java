package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Document(collection = "sdo_recomendations")
public class SdoRecomendations {
    @Id
    private String id;
    @Field("sdo_id")
    private String sdoId;
    @Field("recSdo_id")
    private String recSdoId;
    @Field("recSdo_title")
    private String recSdoTitle;
    @Field("similarity")
    private float similarity;
    @Field("update_time")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSdoId() {
        return sdoId;
    }

    public void setSdoId(String sdoId) {
        this.sdoId = sdoId;
    }

    public String getRecSdoId() {
        return recSdoId;
    }

    public void setRecSdoId(String recSdoId) {
        this.recSdoId = recSdoId;
    }

    public String getRecSdoTitle() {
        return recSdoTitle;
    }

    public void setRecSdoTitle(String recSdoTitle) {
        this.recSdoTitle = recSdoTitle;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
