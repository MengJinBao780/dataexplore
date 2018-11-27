package cn.csdb.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by ajian on 2018/5/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FileInfoServiceTest {

    @Resource
    private FileInfoService fileInfoService;

    @Test
    public void test() {
        String save = fileInfoService.save("xxx鸟");
        System.out.println(save);
    }
}
