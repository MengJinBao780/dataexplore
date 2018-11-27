package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "role")
public class Role {
    @Id
    private String roleId;
    @Field("remark")
    private String remark;
    @Field("create_name")
    private Date createName;
    @Field("role_name")
    private String roleName;
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateName() {
        return createName;
    }

    public void setCreateName(Date createName) {
        this.createName = createName;
    }
}
