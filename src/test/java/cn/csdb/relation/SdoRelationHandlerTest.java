package cn.csdb.relation;

import cn.csdb.service.SdoRelationHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by ajian on 2018/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SdoRelationHandlerTest {
    @Resource
    private SdoRelationHandler sdoRelationHandler;

    /**
     * 关联数据
     *
     */
    @Test
    public void testUpdateHandlerData() {
        System.out.println("start----------------");
        sdoRelationHandler.executeData();
        System.out.println("end------------------");
    }
}
