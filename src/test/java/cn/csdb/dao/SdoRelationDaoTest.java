package cn.csdb.dao;

import cn.csdb.model.SdoRelate;
import cn.csdb.model.SdoRelation;
import cn.csdb.repository.SdoRelationDao;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SdoRelationDaoTest {
    @Resource
    private SdoRelationDao sdoRelationDao;

    @Test
    public void save() {
        SdoRelation sdoRelation = new SdoRelation();
        sdoRelation.setSdoId("1231234234");
        SdoRelate sdoRelate = new SdoRelate();
        sdoRelate.setSdoId("xsdfsdfsdfs");
        sdoRelate.setScore(12.2d);
        sdoRelate.setSdoTitle("你好");
        SdoRelate sdoRelate1 = new SdoRelate();
        sdoRelate1.setSdoId("xsdfsdfsdfs");
        sdoRelate1.setScore(12.2d);
        sdoRelate1.setSdoTitle("你好");
        List<SdoRelate> sdoRelates = Lists.newArrayList(sdoRelate, sdoRelate1);
        sdoRelation.setSdoRelates(sdoRelates);
        sdoRelationDao.save(sdoRelation);
    }

    @Test
    public void find() {
        List<SdoRelation> sdoRelations = sdoRelationDao.getBySdoId("1231234234");
        SdoRelation sdoRelation = sdoRelations.get(0);
        System.out.println(sdoRelation.getSdoRelates().size());
        System.out.println(sdoRelation.getSdoRelates().get(0).getSdoTitle());
    }

    @Test
    public void updateRelate() {
        List<SdoRelation> sdoRelations = sdoRelationDao.getBySdoId("1231234234");
        sdoRelations.get(0).getSdoRelates().get(0).setSdoTitle("xxxxx哈哈");
        sdoRelationDao.updateRelate(sdoRelations.get(0));
    }

    @Test
    public void test1(){
        List<SdoRelation> byPage = sdoRelationDao.getByPage(0, 30);
        System.out.println(byPage.size());
    }
}
