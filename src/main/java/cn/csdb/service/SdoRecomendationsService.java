package cn.csdb.service;

import cn.csdb.model.SdoRecomendations;
import cn.csdb.repository.SdoRecomendationsDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SdoRecomendationsService {
    @Resource
    private SdoRecomendationsDao sdoRecomendationsDao;

    public List<SdoRecomendations> getTopNRecListBySdoId(String sdoId,int n){
        return sdoRecomendationsDao.getTopNRecListBySdoId(sdoId,n);
    }
}
