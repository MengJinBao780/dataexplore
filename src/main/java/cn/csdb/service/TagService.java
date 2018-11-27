package cn.csdb.service;

import cn.csdb.model.Tag;
import cn.csdb.model.TagDetail;
import cn.csdb.repository.TagDao;
import cn.csdb.repository.TagDetailDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {
    @Resource
    private TagDao tagDao;
    @Resource
    private TagDetailDao tagDetailDao;

    public List<Map<String,Object>> getTagsByProd(List<String> prodsName,List<String> prodsId,List<String> imgs){
        List<Map<String,Object>> list = new ArrayList<>();
       // String[] prodsName = new String[]{"卫星","雷达","气候","大气","陆地","6th专项","7th专项","8th专项","9th专项"};
       // String[] prodsId = new String[]{"5ad00ae815d21a7d78215d55","5ad00cd515d21a7d78215d56","5ad00cd515d21a7d78215d57","5ad00cd515d21a7d78215d58","5ad00cd515d21a7d78215d59","5ad00cd515d21a7d78215d5a","5ad00cd515d21a7d78215d5b","5ad00cd515d21a7d78215d5c","5ad00cd515d21a7d78215d5d"};
        for (int i = 0 ; i<prodsId.size();i++) {
            Map<String,Object> map = new HashMap<>();
            List<Tag> tags = tagDao.getTagsByProd(prodsId.get(i));
            List<String> tagNames = new ArrayList<>();
            for (int j= 0 ;j<tags.size();j++){
                tagNames.add(tags.get(j).getTagName());
            }
            map.put("prodName",prodsName.get(i));
            map.put("tagNames",tagNames);
            map.put("prodId",prodsId.get(i));
            map.put("img",imgs.get(i));
            list.add(map);
        }
        return list;
    }

    public List<Map<String,Object>> getTop40Tags(){
        List<Map<String,Object>> tagName = new ArrayList<>();
        List<Tag> tags = tagDao.getTop40Tags();
        for (int i=0;i<tags.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("tagName",tags.get(i).getTagName());
            map.put("weight",(40-i)/2+4);
            tagName.add(map);
        }
        return tagName;
    }

    public boolean hasTag(String tagName,String sdoId){
        List<TagDetail> tagDetails = tagDetailDao.getTagsByName(tagName,sdoId);
        if(tagDetails!=null&&tagDetails.size()>0){
            return true;
        }
        return false;
    }

    public TagDetail addTagDetail(TagDetail tagDetail) {
        return tagDao.addTagDetail(tagDetail);
    }

    public void delTag(String id){
        tagDetailDao.delTag(id);
    }

    public  List<TagDetail> getTagList(int pageNo, int pageSize, int type,String sdoTitle,String prodId,int status){
        int start = pageSize*(pageNo-1);
        return tagDao.getTagList(start,pageSize,type,sdoTitle,prodId,status);
    }


    public long countByPage(int type)
    {
        return tagDao.countByPage(type);
    }

    public boolean approveTag(String id, String auditor){
        boolean b = tagDao.approveTag(id,auditor);
        return b;
    }
    public boolean rejectTag(String id, String auditor){
        boolean b = tagDao.rejectTag(id,auditor);
        return b;
    }

    public String getSdoTitle(String id){
        return tagDao.getSdoTitle(id);
    }

    public String getProName(String id){
        return tagDao.getProName(id);
    }
}
