package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "fileType")
public class FileType {
    @Id
    private String id;
    @Field("sdo_id")
    private String sdoId;
    @Field("ft_name")
    private String ftName;
    @Field("ft_desc")
    private String ftDesc;
    @Field("ft_icon")
    private String ftIcon;
    @Field("ft_phy_type")
    private String ftPhyType;
    @Field("ft_desc_attch")
    private String ftDescAttch;
    @Field("preview_type")
    private String previewType;
    @Field("create_time")
    private Date createTime;
    @Field("update_time")
    private Date updateTime;
    @Field("stat")
    private Integer status;

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

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public String getFtDesc() {
        return ftDesc;
    }

    public void setFtDesc(String ftDesc) {
        this.ftDesc = ftDesc;
    }

    public String getFtIcon() {
        return ftIcon;
    }

    public void setFtIcon(String ftIcon) {
        this.ftIcon = ftIcon;
    }

    public String getFtPhyType() {
        return ftPhyType;
    }

    public void setFtPhyType(String ftPhyType) {
        this.ftPhyType = ftPhyType;
    }

    public String getFtDescAttch() {
        return ftDescAttch;
    }

    public void setFtDescAttch(String ftDescAttch) {
        this.ftDescAttch = ftDescAttch;
    }

    public String getPreviewType() {
        return previewType;
    }

    public void setPreviewType(String previewType) {
        this.previewType = previewType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
