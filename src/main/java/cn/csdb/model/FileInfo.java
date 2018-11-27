package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "fileInfo")
public class FileInfo {
    @Id
    private String id;
    @Field("file_name")
    private String fileName;
    @Field("file_path")
    private String filePath;
    @Field("ft_id")
    private String ftId;
    @Field("ft_name")
    private String ftName;
    @Field("preview_type")
    private String previewType;
    @Field("preview_file")
    private String previewFile;
    @Field("create_time")
    private Date createTime;
    @Field("update_time")
    private Date updateTime;
    @Field("stat")
    private Integer status;
    @Field("pid")
    private String pid;
    @Field("number")
    private String number;
    @Field("cloudiness")
    private Double cloudiness;
    @Field("note")
    private String note;
    @Field("size")
    private String size;
    @Field("record_num")
    private Integer recordNum;
    @Field("data_note")
    private String dataNote;
    @Field("property")
    private String property;
    @Field("center")
    private String center;
    @Field("up_left")
    private String upLeft;
    @Field("up_right")
    private String upRight;
    @Field("low_right")
    private String lowRight;
    @Field("low_left")
    private String lowLeft;
    @Field("begin_time")
    private Date beginTime;
    @Field("end_time")
    private Date endTime;
    @Field("sdo_pid")
    private String sdoPid;
    @Field("cephFlag")
    private int cephFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFtId() {
        return ftId;
    }

    public void setFtId(String ftId) {
        this.ftId = ftId;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public String getPreviewType() {
        return previewType;
    }

    public void setPreviewType(String previewType) {
        this.previewType = previewType;
    }

    public String getPreviewFile() {
        return previewFile;
    }

    public void setPreviewFile(String previewFile) {
        this.previewFile = previewFile;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Double cloudiness) {
        this.cloudiness = cloudiness;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(Integer recordNum) {
        this.recordNum = recordNum;
    }

    public String getDataNote() {
        return dataNote;
    }

    public void setDataNote(String dataNote) {
        this.dataNote = dataNote;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getUpLeft() {
        return upLeft;
    }

    public void setUpLeft(String upLeft) {
        this.upLeft = upLeft;
    }

    public String getUpRight() {
        return upRight;
    }

    public void setUpRight(String upRight) {
        this.upRight = upRight;
    }

    public String getLowRight() {
        return lowRight;
    }

    public void setLowRight(String lowRight) {
        this.lowRight = lowRight;
    }

    public String getLowLeft() {
        return lowLeft;
    }

    public void setLowLeft(String lowLeft) {
        this.lowLeft = lowLeft;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSdoPid() {
        return sdoPid;
    }

    public void setSdoPid(String sdoPid) {
        this.sdoPid = sdoPid;
    }

    public int getCephFlag() {
        return cephFlag;
    }

    public void setCephFlag(int cephFlag) {
        this.cephFlag = cephFlag;
    }

}
