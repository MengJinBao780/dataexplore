package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document(collection = "sdo")
public class Sdo {
    @Id
    private String id;
    @Field("pid")
    private String pid;
    @Field("loginId")
    private String loginId;
    @Field("catalog_id")
    private String catalogId;
    @Field("product_id")
    private String productId;
    @Field("title")
    private String title;
    @Field("desc")
    private String desc;
    @Field("keyword")
    private String keyword;
    @Field("icon_path")
    private String iconPath;
    @Field("creator_organization")
    private List<Organization> creatorOrganization;
    @Field("creator_createTime")
    private Date creatorCreateTime;
    @Field("publisher")
    private Organization publisher;
    @Field("publisher_publishTime")
    private Date publisherPublishTime;
    @Field("scores")
    private Double scores;
    @Field("visit_count")
    private Integer visitCount;
    @Field("download_count")
    private Integer downloadCount;
    @Field("stat")
    private Integer status;
    @Field("taxonomy")
    private String taxonomy;
    @Field("prdTaxonomy")
    private String prdTaxonomy;
    @Field("visitLimit")
    private String visitLimit;
    @Field("rangeDescription")
    private String rangeDescription;
    @Field("center")
    private String center;
    @Field("upLeft")
    private String upLeft;
    @Field("lowLeft")
    private String lowLeft;
    @Field("upRight")
    private String upRight;
    @Field("lowRight")
    private String lowRight;
    @Field("startTime")
    private Date startTime;
    @Field("endTime")
    private Date endTime;
    @Field("spatial_resolution")
    private String spatialResolution;
    @Field("time_resolution")
    private String timeResolution;
    @Field("rightstatement")
    private String rightstatement;
    @Field("toMemorySize")
    private Double toMemorySize;
    @Field("toFilesNumber")
    private Integer toFilesNumber;
    @Field("toRecordNumber")
    private Integer toRecordNumber;
    @Field("tags")
    private List<String> tags;

    @Field("dataFormat")
    private String dataFormat;
    /**
     * sdo创建时间，即插入数据库时间，用于关联图谱的已完成任务标识
     */
    @Field("create_time")
    private Date createTime;



    //20180625增加
    @Field("citeAddress")
    private String citeAddress;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public List<Organization> getCreatorOrganization() {
        return creatorOrganization;
    }

    public void setCreatorOrganization(List<Organization> creatorOrganization) {
        this.creatorOrganization = creatorOrganization;
    }

    public Date getCreatorCreateTime() {
        return creatorCreateTime;
    }

    public void setCreatorCreateTime(Date creatorCreateTime) {
        this.creatorCreateTime = creatorCreateTime;
    }

    public Organization getPublisher() {
        return publisher;
    }

    public void setPublisher(Organization publisher) {
        this.publisher = publisher;
    }

    public Date getPublisherPublishTime() {
        return publisherPublishTime;
    }

    public void setPublisherPublishTime(Date publisherPublishTime) {
        this.publisherPublishTime = publisherPublishTime;
    }

    public Double getScores() {
        return scores;
    }

    public void setScores(Double scores) {
        this.scores = scores;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getPrdTaxonomy() {
        return prdTaxonomy;
    }

    public void setPrdTaxonomy(String prdTaxonomy) {
        this.prdTaxonomy = prdTaxonomy;
    }

    public String getVisitLimit() {
        return visitLimit;
    }

    public void setVisitLimit(String visitLimit) {
        this.visitLimit = visitLimit;
    }

    public String getRangeDescription() {
        return rangeDescription;
    }

    public void setRangeDescription(String rangeDescription) {
        this.rangeDescription = rangeDescription;
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

    public String getLowLeft() {
        return lowLeft;
    }

    public void setLowLeft(String lowLeft) {
        this.lowLeft = lowLeft;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSpatialResolution() {
        return spatialResolution;
    }

    public void setSpatialResolution(String spatialResolution) {
        this.spatialResolution = spatialResolution;
    }

    public String getTimeResolution() {
        return timeResolution;
    }

    public void setTimeResolution(String timeResolution) {
        this.timeResolution = timeResolution;
    }

    public String getRightstatement() {
        return rightstatement;
    }

    public void setRightstatement(String rightstatement) {
        this.rightstatement = rightstatement;
    }

    public Double getToMemorySize() {
        return toMemorySize;
    }

    public void setToMemorySize(Double toMemorySize) {
        this.toMemorySize = toMemorySize;
    }

    public Integer getToFilesNumber() {
        return toFilesNumber;
    }

    public void setToFilesNumber(Integer toFilesNumber) {
        this.toFilesNumber = toFilesNumber;
    }

    public Integer getToRecordNumber() {
        return toRecordNumber;
    }

    public void setToRecordNumber(Integer toRecordNumber) {
        this.toRecordNumber = toRecordNumber;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getCiteAddress() {
        return citeAddress;
    }

    public void setCiteAddress(String citeAddress) {
        this.citeAddress = citeAddress;
    }

}
