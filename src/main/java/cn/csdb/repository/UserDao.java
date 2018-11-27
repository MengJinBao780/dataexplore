package cn.csdb.repository;

import cn.csdb.model.Role;
import cn.csdb.model.User;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huangwei on 2018/6/8.
 */
@Repository
public class UserDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public boolean isAdmin(String loginid){
        //User user = mongoTemplate.findById(loginid, User.class);
        User user = getUserById(loginid);

        if(user == null){
            return false;
        }
        String roleName = user.getRoles().get(0).getRoleName();
        if(roleName.equals("admin")){
            return true;
        }else{
            return false;
        }
    }

    public User getUserById(String loginid){
        DBObject query = QueryBuilder.start().and("loginId").is(loginid).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<User> users = mongoTemplate.find(basicQuery,User.class);
        if(users.size() == 0){
            return null;
        }else{
            return users.get(0);
        }
    }

    public void addGuestUser(String loginId,String trueName){
        User user = new User();
        user.setLoginId(loginId);
        user.setTrueName(trueName);
        List<Role> roles = getRole("guest");
        user.setRoles(roles);
        mongoTemplate.save(user);
    }

    public List<Role> getRole(String roleName){
        DBObject query = QueryBuilder.start().and("role_name").is(roleName).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Role> roles = mongoTemplate.find(basicQuery,Role.class);
        return roles;
    }

    public long countUsersNum(){
        long count = mongoTemplate.count(new Query(),"user");
        return count;
    }
}

