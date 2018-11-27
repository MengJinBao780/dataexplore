package cn.csdb.service;

import cn.csdb.model.TagDetail;
import cn.csdb.repository.TagDetailDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagDetailService {
    @Resource
    private TagDetailDao tagDetailDao;

    //用户打标签
    public boolean addTag(String loginId,String sdoId,String tagName,String prodId){
        List<TagDetail> tagDetails = tagDetailDao.selTag(loginId,sdoId,tagName);
        if (tagDetails == null ||tagDetails.size()==0){
            tagDetailDao.addTag(prodId,sdoId,tagName,loginId);
            return true;
        }
        return false;
    }
    //用户删除标签
    public boolean delTag(String loginId,String sdoId,String tagName){
        List<TagDetail> tagDetails = tagDetailDao.selTag(loginId,sdoId,tagName);
        if (tagDetails == null ||tagDetails.size()==0){
            return false;
        }
        for (int i=0;i<tagDetails.size();i++){
            tagDetailDao.delTag(tagDetails.get(i).getId());
        }
        return true;
    }

    //查询用户对某数据打的所有标签（未审核）
    public Map<String,Object> selTag(String loginId,String sdoId){
        Map<String,Object> map = new HashMap<>();
        List<TagDetail> list= tagDetailDao.selTag(loginId,sdoId);
        if (list==null || list.size()==0){
            return map;
        }
        List<String> strings = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            strings.add(list.get(i).getTagName());
        }
        map.put("tag",strings);
        return map;
    }

    public TagDetail getTagDetail(String id){
        return tagDetailDao.getTagDetail(id);
    }
}
