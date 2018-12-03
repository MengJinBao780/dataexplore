package cn.csdb.service;

import cn.csdb.model.Sdo;
import cn.csdb.model.SdoDownload;
import cn.csdb.model.SdoFavorites;
import cn.csdb.repository.SdoDao;
import cn.csdb.repository.SdoDownloadDao;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SdoDownloadService {
    @Resource
    private SdoDownloadDao sdoDownloadDao;

    @Resource
    private SdoDao sdoDao;

    //添加下载日志
    public void addLog(String loginId,String fileId,String fileName,String sdoId,String title){
        sdoDownloadDao.addLog(loginId,fileId,fileName,sdoId,title);
    }


    public List<SdoDownload> getList(String loginId, int start, int pageSize){
        List<SdoDownload> list = sdoDownloadDao.getList(loginId,start,pageSize);
        for (SdoDownload sdoDownload : list){
            String sdoId = sdoDownload.getSdoId();
            if (StringUtils.isNotEmpty(sdoId)){
                cn.csdb.model.Resource sdo = sdoDao.getSdoById(sdoId);
                sdoDownload.setResName(sdo.getTitle());
            }
            //sdoDownload.setResName(sdoDao.getSdoById(sdoDownload.getSdoId()).getTitle());
        }

        List<String> sdoIdList = Lists.transform(list, new Function<SdoDownload, String>() {
            @Override
            public String apply(SdoDownload s) {

                return s.getSdoId();
            }

        });

        List<Sdo> sdos = sdoDao.getSdosByIds(sdoIdList);
        for(int i = 0;i<list.size();i++){
            String id = list.get(i).getSdoId();
            for(int j=0;j<sdos.size();j++){
                if(id.equals(sdos.get(j).getId())){
                    SdoDownload sdoDownload = list.get(i);
                    Sdo sdo = sdos.get(j);
                    sdoDownload.setDesc(sdo.getDesc());
                    sdoDownload.setDownloadCount(sdo.getDownloadCount());
                    sdoDownload.setToFilesNumber(sdo.getToFilesNumber());
                    sdoDownload.setPublisher(sdo.getPublisher());
                    sdoDownload.setDataFormat(sdo.getDataFormat());
                    sdoDownload.setIconPath(sdo.getIconPath());
                }
            }
        }
        return list;
    }

    //统计用户下载的记录条数
    public long getCount(String loginId){
        return sdoDownloadDao.getCount(loginId);
    }

    public Map<String,Object> getBy(String title , String loginId , Date beginTime, Date endTime, int pageNum){
        List<SdoDownload> list = sdoDownloadDao.getBy(title,loginId,beginTime,endTime);
        Map<String,Object> map = new HashMap<>();
        int totalCount = list.size();
        int totalPage = totalCount%10==0?totalCount/10:totalCount/10+1;
        if(pageNum>totalPage){
            return map;
        }
        List<SdoDownload> result ;
        if (pageNum==totalPage){
            result =list.subList((pageNum-1)*10,totalCount);
        }else{
            result = list.subList((pageNum-1)*10,pageNum*10);
        }
        map.put("result",result);
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("pageNum",pageNum);
        return map;
    }

    public long getTotalCount(){
        return sdoDownloadDao.getTotalCount();
    }
}
