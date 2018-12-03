package cn.csdb.service;

import cn.csdb.model.Sdo;
import cn.csdb.model.SdoFavorites;
import cn.csdb.repository.SdoDao;
import cn.csdb.repository.SdoFavoritesDao;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SdoFavoritesService {
    @Resource
    private SdoFavoritesDao sdoFavoritesDao;

    @Resource
    private SdoDao sdoDao;


    //取消收藏，就变更isValid值的方案
    //添加sdo到favorite表中
    public int addData(String loginId,String sdoId,String title){
        return sdoFavoritesDao.addData(loginId,sdoId,title);
    }

    //更新favorite表中记录
    public int updateFavorite(String loginId,String sdoId,Integer isValid ){
        return sdoFavoritesDao.updateFavorite(loginId, sdoId, isValid);
    }

    //判断favorite表中是否有某个sdo信息
    public int hasFavorite(String loginId,String sdoId){
        return sdoFavoritesDao.hasFavorite(loginId,sdoId);
    }

    //由loginId和sdoId获得sdoFavorite
    public SdoFavorites getByLoginIdAndSdoId(String loginId,String sdoId){
        return sdoFavoritesDao.getByLoginIdAndSdoId(loginId,sdoId);
    }


    //byCql

    /*取消收藏即删除的方案
    //添加收藏
    public int addData(String loginId,String sdoId){
        return sdoFavoritesDao.addData(loginId,sdoId);
    }

    //删除收藏
    public int delete(String loginId,String sdoId){
        return sdoFavoritesDao.deleteData(loginId,sdoId);
    }

    //判断是否收藏
    public int hasFavorite(String loginId,String sdoId){
        return sdoFavoritesDao.hasFavorite(loginId,sdoId);
    }

    //获取某个收藏数据
    public SdoFavorites getByLoginIdAndSdoId(String loginId, String sdoId){
        return sdoFavoritesDao.getByLoginIdAndSdoId(loginId,sdoId);
    }
    */


    //返回收藏或者取消收藏结果
    public Map<String,String> getResult(String loginId,String sdoId,Integer isValid,String title){
        Map<String,String> map = new HashMap<>();
        Integer status = sdoFavoritesDao.checkIsValid(loginId,sdoId);
        if (status ==2 && isValid ==0){
            map.put("result","您没有收藏，不能取消收藏");
            return map;
        }
        if (status ==2 && isValid ==1){
            sdoFavoritesDao.addData(loginId,sdoId,title);
            map.put("result","收藏成功");
            return map;
        }
        if (status==1 && isValid ==1){
            map.put("result","您已经收藏，不需要重复收藏");
            return map;
        }
        if (status==1 && isValid ==0){
            sdoFavoritesDao.updateIsValid(loginId,sdoId,0);
            map.put("result","取消收藏成功");
            return map;
        }
        if (status==0 && isValid ==0){
            map.put("result","您没有收藏，不能取消收藏");
            return map;
        }
        if (status==0 && isValid ==1){
            sdoFavoritesDao.updateIsValid(loginId,sdoId,1);
            map.put("result","收藏成功");
            return map;
        }
        map.put("result","操作有误");
        return  map;
    }

    //判断是否收藏该数据
    public Integer checkIsValid(String loginId ,String sdoId){
       return sdoFavoritesDao.checkIsValid(loginId,sdoId);
    }


    //统计用户收藏的记录条数
    public long getCount(String loginId){
        return sdoFavoritesDao.getCount(loginId);
    }

    public List<SdoFavorites> getList(String loginId, int start, int pageSize){
        //return sdoFavoritesDao.getList(loginId,start,pageSize);
        final List<SdoFavorites> list = sdoFavoritesDao.getList(loginId,start,pageSize);
        for (SdoFavorites sdoFavorites : list){
            cn.csdb.model.Resource sdo = sdoDao.getSdoById(sdoFavorites.getSdoId());
            sdoFavorites.setResName(sdo.getTitle());
        }
        List<String> sdoIdList = Lists.transform(list, new Function<SdoFavorites, String>() {
            @Override
            public String apply(SdoFavorites s) {

                return s.getSdoId();
            }

        });

        List<Sdo> sdos = sdoDao.getSdosByIds(sdoIdList);
        for(int i = 0;i<list.size();i++){
            String id = list.get(i).getSdoId();
            for(int j=0;j<sdos.size();j++){
                if(id.equals(sdos.get(j).getId())){
                    SdoFavorites sdoFavorites = list.get(i);
                    Sdo sdo = sdos.get(j);
                    sdoFavorites.setDesc(sdo.getDesc());
                    sdoFavorites.setDownloadCount(sdo.getDownloadCount());
                    sdoFavorites.setToFilesNumber(sdo.getToFilesNumber());
                    sdoFavorites.setPublisher(sdo.getPublisher());
                    sdoFavorites.setDataFormat(sdo.getDataFormat());
                    sdoFavorites.setIconPath(sdo.getIconPath());
                }
            }
        }
        return list;
    }


    public int deleteFavorite(String id) {
        return sdoFavoritesDao.deleteFavorite(id);
    }

    public Map<String,Object> getBy(String title , String loginId , Date beginTime, Date endTime, int pageNum){
        List<SdoFavorites> list = sdoFavoritesDao.getBy(title,loginId,beginTime,endTime);
        Map<String,Object> map = new HashMap<>();
        int totalCount = list.size();
        int totalPage = totalCount%10==0?totalCount/10:totalCount/10+1;
        if(pageNum>totalPage){
            return map;
        }
        List<SdoFavorites> result ;
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
