package cn.csdb.job;

import cn.csdb.repository.*;
import cn.csdb.service.SdoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by huangwei on 2018/6/23.
 */
@Service
public class StatSchedule {

    @Resource
    private UserDao userDao;

    @Resource
    private FileInfoDao fileInfoDao;

    @Resource
    private SdoVisitDao sdoVisitDao;

    @Resource
    private SdoDownloadDao sdoDownloadDao;

    @Resource
    private SdoDao sdoDao;

    @Resource
    private  StatDao statDao;

    @Resource
    private SdoService sdoService;

    public void statMethod() {
        //    计算用户数量
        long countUsersNum = userDao.countUsersNum();
        //    计算文件数量
        long countfiles = fileInfoDao.countFiles();
        //    计算数据集数量
        long countSdos = sdoDao.countSdos();
        //    计算访问量数量
        long countSdoVisits = sdoVisitDao.countSdoVisits();
        //    计算下载量数量
        long countDownloads = sdoDownloadDao.getTotalCount();
        //    计算数据量
        long countFileSize = sdoService.countFileSize();
        countFileSize = countFileSize / 1024L / 1024L;
        statDao.addData(String.valueOf(countUsersNum),String.valueOf(countSdos),String.valueOf(countFileSize),String.valueOf(countDownloads),String.valueOf(countfiles),String.valueOf(countSdoVisits));
    }

}
