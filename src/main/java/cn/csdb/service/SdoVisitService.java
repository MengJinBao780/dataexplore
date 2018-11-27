package cn.csdb.service;

import cn.csdb.model.SdoVisit;
import cn.csdb.repository.SdoVisitDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SdoVisitService {
    @Resource
    private SdoVisitDao sdoVisitDao;

    //添加访问日志
    public void addLog(String loginId,String sdoId,String title){
        sdoVisitDao.addLog(loginId,sdoId,title);
    }

    //获取第几页数据
    public Map<String,Object> getBy(String title , String loginId , Date beginTime, Date endTime,int pageNum){
        List<SdoVisit> list = sdoVisitDao.getBy(title,loginId,beginTime,endTime);
        Map<String,Object> map = new HashMap<>();
        int totalCount = list.size();
        int totalPage = totalCount%10==0?totalCount/10:totalCount/10+1;
        if(pageNum>totalPage){
            return map;
        }
        List<SdoVisit> result ;
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
}
