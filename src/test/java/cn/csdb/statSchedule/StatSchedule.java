package cn.csdb.statSchedule;

import cn.csdb.model.FileInfo;
import cn.csdb.model.Sdo;
import cn.csdb.model.SdoDownload;
import cn.csdb.model.User;
import cn.csdb.repository.*;
import cn.csdb.service.SdoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by huangwei on 2018/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class StatSchedule {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private FileInfoDao fileInfoDao;

    @Resource
    private SdoVisitDao sdoVisitDao;

    @Resource
    private SdoDownloadDao sdoDownloadDao;

    @Resource
    private SdoDao sdoDao;

    @Resource
    private SdoService sdoService;
//    计算用户数量
    @Test
    public void countUsersNum(){
        long  l= mongoTemplate.count(new Query(),User.class);
        System.out.println("用户"+l);
    }

    /*@Test
    public void conntFilesSize(){
        Date starttime = new Date();
        List<FileInfo> fileInfoList = fileInfoDao.countFilesSize(starttime);
        double d = 0.0;
        for(int i=0;i<fileInfoList.size();i++){
            d += Double.parseDouble(fileInfoList.get(i).getSize());
        }
        System.out.println(d);
    }*/

    //    计算文件数量
    @Test
    public void countfiles(){
        long l = fileInfoDao.countFiles();
        System.out.println("文件"+l+"个");
    }

    //    计算数据集数量
    @Test
    public void countSdos(){
        long l = sdoDao.countSdos();
        System.out.println("数据集"+l+"个");

    }

    //    计算访问量数量
    @Test
    public void countSdoVisits(){
        long l = sdoVisitDao.countSdoVisits();
        System.out.println("访问量"+l+"次");

    }

    //    计算下载量数量
    @Test
    public void countDownloads(){
        long l = sdoDownloadDao.getTotalCount();
        System.out.println("下载"+l+"次");

    }


    //    计算数据量
    @Test
    public void countFileSize(){
        long l = sdoService.countFileSize();
        l = l/1024L/1024L;
        System.out.println("数据量"+l+"GB");

    }
}
