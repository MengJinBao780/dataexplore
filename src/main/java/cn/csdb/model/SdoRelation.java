package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by ajian on 2018/5/15.
 */
@Document(collection = "sdo_relation")
public class SdoRelation {

    @Id
    private String id;
    @Field("sdo_id")
    private String sdoId;
    @Field("last_create_time")
    private Date lastCreateTime;
    @Field("create_time")
    private Date createTime;
    @Field("sdo_relates")
    List<SdoRelate> sdoRelates;

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

    public List<SdoRelate> getSdoRelates() {
        return sdoRelates;
    }

    public void setSdoRelates(List<SdoRelate> sdoRelates) {
        this.sdoRelates = sdoRelates;
    }

    public Date getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(Date lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
