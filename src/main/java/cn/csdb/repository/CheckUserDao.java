package cn.csdb.repository;

import cn.csdb.model.Subject;
import cn.csdb.model.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shiba on 2018/11/6.
 */
@Repository
public class CheckUserDao {
    @Resource
    private MongoTemplate mongoTemplate;
    /**
     *  通过用户名查找用户
     *  @param LoginId
     *  @return User
     */
    public User getByUserName(String LoginId){
        return mongoTemplate.find(new Query(Criteria.where("loginId").is(LoginId)),User.class).get(0);
    }

    public Subject getSubjectByCode(String subjectCode){
        List<Subject>sub = mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subjectCode)),Subject.class);
        if(sub.size()==0){
            return null;
        }else {
            return mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subjectCode)), Subject.class).get(0);
        }
    }

    /**
     *  通过用户名查找该用户所有的角色并保存在Set集合中
     *  @param username
     *  @return Set<String>
     */
    /*public Set<String> getRoles(String username){

    }*/

    /**
     *  通过用户名查找该用户所有的权限并保存在Set集合中
     *  @param username
     *  @return Set<String>
     */
    /*public Set<String> getPermissions(String username){

    }*/

}
