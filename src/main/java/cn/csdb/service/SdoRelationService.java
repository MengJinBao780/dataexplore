package cn.csdb.service;

import cn.csdb.model.SdoRelate;
import cn.csdb.model.SdoRelation;
import cn.csdb.repository.SdoRelationDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/16.
 */
@Service
public class SdoRelationService {

    @Resource
    private SdoRelationDao sdoRelationDao;

    public List<SdoRelate> relationList(String sdoId) {
        List<SdoRelation> sdoRelations = sdoRelationDao.getBySdoId(sdoId);
        if (sdoRelations == null || sdoRelations.size() == 0) {
            return null;
        }
        return sdoRelations.get(0).getSdoRelates();
    }
}
