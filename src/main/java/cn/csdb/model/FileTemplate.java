package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "file_template")
public class FileTemplate {
    @Id
    private String id;
    @Field("file_type")
    private String fileType;
    @Field("field_name")
    private String fieldName;
    @Field("field_title")
    private String fieldTitle;
    @Field("field_type")
    private String fieldType;
    @Field("is_search")
    private Integer isSearch;
    @Field("is_show")
    private Integer isShow;
    @Field("sort_order")
    private Integer sortOrder;
    @Field("is_preview")
    private Integer isPreview;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(Integer isSearch) {
        this.isSearch = isSearch;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Integer isPreview) {
        this.isPreview = isPreview;
    }
}
