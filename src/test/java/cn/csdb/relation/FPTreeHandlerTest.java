package cn.csdb.relation;

import cn.csdb.service.FPTreeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by ajian on 2018/5/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FPTreeHandlerTest {

    @Resource
    private FPTreeHandler fpTreeHandler;

    /**
     * 用户访问记录，频繁项集发现，完成当前记录的20条推荐数据，并写入数据库
     */
    @Test
    public void createVisitor() {
        fpTreeHandler.execute();
    }


}
