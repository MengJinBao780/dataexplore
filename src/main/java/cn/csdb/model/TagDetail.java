package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "tagdetail")
public class TagDetail {
    @Id
    private String id;
    @Field("prod_id")
    private String prodId;
    @Field("sdo_id")
    private String sdoId;
    @Field("tag_name")
    private String tagName;
    @Field("login_id")
    private String loginId;
    @Field("stat")
    private Integer status;
    @Field("audit_date")
    private Date auditDate;
    @Field("auditor")
    private String auditor;

    private String prodName;
    @Field("sdoTitle")
    private String sdoTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getSdoId() {
        return sdoId;
    }

    public void setSdoId(String sdoId) {
        this.sdoId = sdoId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getProdName() {
        return prodName;
    }

    public String getSdoTitle() {
        return sdoTitle;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setSdoTitle(String sdoTitle) {
        this.sdoTitle = sdoTitle;
    }
}
