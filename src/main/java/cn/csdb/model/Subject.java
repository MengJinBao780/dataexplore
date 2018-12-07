package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "t_subject")
public class Subject {
    @Id
    private String id;
    @Field("subjectName")
    private String subjectName;
    @Field("subjectCode")
    private String subjectCode;
    @Field("imagePath")
    private String imagePath;
    @Field("brief")
    private String brief;
    @Field("admin")
    private String admin;
    @Field("adminPasswd")
    private String adminPasswd;
    @Field("contact")
    private String contact;
    @Field("phone")
    private String phone;
    @Field("email")
    private String email;
    @Field("ftpUser")
    private String ftpUser;
    @Field("ftpPassword")
    private String ftpPassword;
    @Field("SerialNo")
    private String serialNo;
    @Field("ftpFilePath")
    private String ftpFilePath;
    @Field("dbName")
    private String dbName;
    @Field("dbUserName")
    private String dbUserName;
    @Field("dbPassword")
    private String dbPassword;
    @Field("dbHost")
    private String dbHost;
    @Field("dbPort")
    private String dbPort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdminPasswd() {
        return adminPasswd;
    }

    public void setAdminPasswd(String adminPasswd) {
        this.adminPasswd = adminPasswd;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getFtpFilePath() {
        return ftpFilePath;
    }

    public void setFtpFilePath(String ftpFilePath) {
        this.ftpFilePath = ftpFilePath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", brief='" + brief + '\'' +
                ", admin='" + admin + '\'' +
                ", adminPasswd='" + adminPasswd + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", ftpUser='" + ftpUser + '\'' +
                ", ftpPassword='" + ftpPassword + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", ftpFilePath='" + ftpFilePath + '\'' +
                ", dbName='" + dbName + '\'' +
                ", dbUserName='" + dbUserName + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", dbHost='" + dbHost + '\'' +
                ", dbPort='" + dbPort + '\'' +
                '}';
    }
}
