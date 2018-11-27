package cn.csdb.db;

import cn.csdb.model.Sdo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class DateTimeTest {

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void test(){
        Sdo sdo = new Sdo();
        sdo.setTitle("aaa2019");
        sdo.setCreateTime(DateUtils.addHours(new Date(),8));
        mongoTemplate.save(sdo);
    }
}
