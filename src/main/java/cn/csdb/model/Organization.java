package cn.csdb.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class Organization {
    @Field("organization")
    private String organization;
    @Field("name")
    private String name;
    @Field("email")
    private String email;
    @Field("telPhone")
    private String telPhone;
    @Field("address")
    private String address;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
