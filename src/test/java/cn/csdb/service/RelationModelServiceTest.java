package cn.csdb.service;

import cn.csdb.model.Sdo;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RelationModelServiceTest {

    @Resource
    private RelationModelService relationModelService;

    @Test
    public void test1() {
//      List<String> strings = Lists.newArrayList("5aceb9779ef710073861fba1", "5aceb96b9ef710073861fb9d", "5aceb97e9ef710073861fba7");
        List<Sdo> realtions = relationModelService.getRelations("5aceb97e9ef710073861fba7", 10);
        System.out.println(realtions.size());
        System.out.println(realtions.get(0).getTitle());
        System.out.println(realtions.get(0).getCenter());
        System.out.println(realtions.get(0).getId());
    }

}
