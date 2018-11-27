package cn.csdb.service;

import cn.csdb.model.Sdo;
import cn.csdb.repository.RelationModelDao;
import cn.csdb.repository.SdoDao;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/29.
 */
@Service
public class RelationModelService {

    @Resource
    private RelationModelDao relationModelDao;
    @Resource
    private SdoDao sdoDao;

    public List<Sdo> getRelations(String sdoId, int count) {
        List<String> list = relationModelDao.getRelationsBySdoId(sdoId);
        if (list == null || list.size() == 0) {
            return Lists.newArrayList();
        }
        List<String> sdoList = list.size() > count ? list.subList(0, count) : list;
        return sdoDao.getSdoByIds(sdoList);
    }
}
