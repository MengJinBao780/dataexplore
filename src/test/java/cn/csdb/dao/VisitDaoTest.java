package cn.csdb.dao;

import cn.csdb.model.SdoVisit;
import cn.csdb.repository.SdoVisitDao;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class VisitDaoTest {
    @Resource
    private SdoVisitDao sdoVisitDao;

    @Test
    public void test() {
        List<SdoVisit> all = sdoVisitDao.getAll();
        int countUser = 20;
//        int i = 0;
        for (SdoVisit visit : all) {
            visit.setLoginId(RandomUtils.nextInt(0, countUser) + "");
            sdoVisitDao.update(visit);
        }
    }

    @Test
    public void test2() {
        List<String> visitorList = sdoVisitDao.getAllLoginId();
        System.out.println("visitor count:" + visitorList.size());
        String s = visitorList.get(0);
        System.out.println(s);
    }

    @Test
    public void test3() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 0 - 1);
        List<SdoVisit> sdoVisits = sdoVisitDao.getSdoVisits("0", calendar.getTime());
        System.out.println(sdoVisits.size());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(sdoVisits.get(0).getCreateTime()));
    }

}
