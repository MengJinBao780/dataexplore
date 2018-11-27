package cn.csdb.service;

import cn.csdb.model.Stat;
import cn.csdb.repository.StatDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by huangwei on 2018/4/16.
 */
@Service
public class StatService {

    @Resource
    private StatDao statDao;

    public void createCollection() {
        statDao.createCollection();
    }

    public void saveStat(Stat stat) {
        statDao.saveStat(stat);
    }

    public Stat getLastStat(){
        return statDao.getLastStat();
    }
}
