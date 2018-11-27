package cn.csdb.db;

import cn.csdb.model.User;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 高级查询操作示例
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserQueryTest {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * where查询,查询结果中，筛选过滤掉某些field
     */
    @Test
    public void selectTest2() {
        DBObject query = QueryBuilder.start()
                .and("trueName")
                .is("wzj")
                .get();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("trueName", true);//需要获取trueName字段
        basicDBObject.put("_id", false);//不需要获取该字段，默认需要
        BasicQuery basicQuery = new BasicQuery(query, basicDBObject);
        List<User> users = mongoTemplate.find(basicQuery, User.class);
        System.out.println(users.size());
        for (User user : users) {
            System.out.println(user.getTrueName());
            System.out.println(user.getId());
        }
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPage() {
        DBObject query = QueryBuilder.start()
                .and("trueName")
                .is("wzj")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(1);
        basicQuery.limit(5);
        List<User> users = mongoTemplate.find(basicQuery, User.class);
        System.out.println(users.size());
        for (User user : users) {
            System.out.println(user.getTrueName());
            System.out.println(user.getId());
        }
    }

    /**
     * 分页查询,并排序
     */
    @Test
    public void findByPageAndOrder() {
        DBObject query = QueryBuilder.start()
                .and("stat")
                .is(1)
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(1);
        basicQuery.limit(5);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "_id");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));

        List<User> users = mongoTemplate.find(basicQuery, User.class);
        System.out.println(users.size());
        for (User user : users) {
            System.out.println(user.getId());
        }
    }

    /**
     * 查询：某集合内的集合字段作为查询条件
     */
    @Test
    public void findByCollFields() {
        DBObject query = QueryBuilder.start()
                .and("roles.role_name")
                .is("guest")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(5);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "_id");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));

        List<User> users = mongoTemplate.find(basicQuery, User.class);
        System.out.println(users.size());
        for (User user : users) {
            System.out.println(user.getId());
        }
    }

    /**
     * 查询2：某集合内的集合字段作为查询条件
     */
    @Test
    public void findByCollFields2() {
        DBObject query = QueryBuilder.start()
                .and("roles._id")
                .is("b3fdd6cb-ad52-40a1-b38f-ec6167ce9e2b")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(5);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "_id");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));

        List<User> users = mongoTemplate.find(basicQuery, User.class);
        System.out.println(users.size());
        for (User user : users) {
            System.out.println(user.getId());
        }
    }

}

