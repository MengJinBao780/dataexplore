package cn.csdb.db;

import cn.csdb.model.User;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 更新操作
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserUpdateTest {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 简单更新，
     * updateFirst只更新一条数据
     * updateMulti更新多条数据
     */
    @Test
    public void updateTest() {
        DBObject query = QueryBuilder.start()
                .and("trueName")
                .is("wzj")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        Update stat = Update.update("stat", "2");
        WriteResult writeResult = mongoTemplate.updateFirst(basicQuery, stat, User.class);
        System.out.println("更新数据条数：" + writeResult.getN());
    }

    @Test
    public void updateTest1() {
//        mongoTemplate.up()
    }

}
