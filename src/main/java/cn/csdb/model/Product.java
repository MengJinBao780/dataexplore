package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "product")
public class Product {
    @Id
    private String id;
    @Field("prod_name")
    private String prodName;
    @Field("prod_desc")
    private String prodDesc;
    @Field("prod_icon_path")
    private String prodIconPath;
    @Field("parent_id")
    private String parentId;
    @Field("is_leaf")
    private Integer isLeaf;
    @Field("order")
    private Integer order;
    @Field("stat")
    private Integer status;
    @Field("storageCapacity")
    private String storageCapacity;
    @Field("fileNum")
    private String fileNum;
    @Field("datasetNum")
    private String datasetNum;

    public String getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(String storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public String getDatasetNum() {
        return datasetNum;
    }

    public void setDatasetNum(String datasetNum) {
        this.datasetNum = datasetNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdIconPath() {
        return prodIconPath;
    }

    public void setProdIconPath(String prodIconPath) {
        this.prodIconPath = prodIconPath;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
