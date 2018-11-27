package cn.csdb.db;

import cn.csdb.model.Role;
import cn.csdb.model.User;
import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 基本操作示例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserSimpleTest {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 生成collection
     */
    @Test
    public void createCollection() {
        DBCollection user = mongoTemplate.createCollection("user");
        user.count();
    }

    /**
     * 保存一条数据
     */
    @Test
    public void saveUser() {
        User user = new User();
        user.setLoginId("bbbbb");
        user.setStatus(1);
        user.setTrueName("bbbbb");
        mongoTemplate.save(user);
    }

    /**
     * 可代码中自定义生成uuid，会替代数据库生成uuid，用作数据主键
     * 当collection中存在该uuid时，使用save()会对数据进行更新,使用insert()会报异常信息
     */
    @Test
    public void saveUserIncludeRoles() {
        User user = new User();
        user.setLoginId("test2");
        user.setStatus(1);
        user.setTrueName("测试用户");

        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleName("user");
        role.setRemark("普通用户");
        role.setCreateName(new Date());
        System.out.println(role.getRoleId());
        mongoTemplate.save(role);
        List<Role> roles = Lists.newArrayList(role);
        user.setRoles(roles);
        mongoTemplate.save(user);
    }

    /**
     * 获取所有数据
     */
    @Test
    public void getUsers() {
        List<User> userList = mongoTemplate.findAll(User.class);
        for (User user : userList) {
            System.out.println(user.getId());
            System.out.println(user.getLoginId());
            if (user.getRoles() != null) {
                System.out.println(user.getRoles().size());
            }
        }
    }

    /**
     * 根据id获取collection
     */
    @Test
    public void getFieldUser() {
        String id = "";
        mongoTemplate.findById(id, User.class);
    }

    /**
     * where查询
     * 注意：mongodb的字段名称大小写敏感
     */
    @Test
    public void selectTest() {
        List<User> users = mongoTemplate.find(
                new Query(
                        Criteria.where("trueName").is("wzj")),
                User.class);
        System.out.println(users.size());
    }

    /**
     * where查询，多条件关联
     */
    @Test
    public void selectTest1() {
        List<User> users = mongoTemplate.find(
                new Query(
                        Criteria.where("trueName").is("wzj").and("stat").is(1)),
                User.class);
        System.out.println(users.size());
    }

}

