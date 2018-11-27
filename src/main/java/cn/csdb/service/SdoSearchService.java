package cn.csdb.service;

import cn.csdb.model.SdoSearch;
import cn.csdb.repository.SdoSearchDao;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SdoSearchService {

    @Resource
    private SdoSearchDao sdoSearchDao;

    public void add(String loginId, String keyword){
        sdoSearchDao.save(loginId,keyword);
    }

    public List<BasicDBObject> getSearchWord(){
        return sdoSearchDao.getSearchWord().getMappedResults();

    }

    //页码数据
    public Map<String,Object> getBy(String keyword , String loginId , Date beginTime, Date endTime, int pageNum){
        List<SdoSearch> list = sdoSearchDao.getBy(keyword,loginId,beginTime,endTime);
        Map<String,Object> map = new HashMap<>();
        int totalCount = list.size();
        int totalPage = totalCount%10==0?totalCount/10:totalCount/10+1;
        if(pageNum>totalPage){
            return map;
        }
        List<SdoSearch> result ;
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
