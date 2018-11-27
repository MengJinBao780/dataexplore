package cn.csdb.service;

import cn.csdb.model.*;
import cn.csdb.repository.ContentEditDao;
import cn.csdb.repository.ContentEditTypeDao;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sophie on 2017/11/8.
 */
@Service
public class ContentEditService {
    @Resource
    private ContentEditDao collectionDao;
    @Resource
    private ContentEditTypeDao categoryDao;

    @Transactional(readOnly = true)
    public List<ContentEdit> getListByPage(String colType,int pageNo, int pageSize){
        List<ContentEdit> list = collectionDao.getListByPage(colType,(pageNo-1)*pageSize,pageSize);
        return list;
    }
    @Transactional(readOnly = true)
    public long countByPage(String colType)
    {
        return collectionDao.countByPage(colType);
    }

    @Transactional
    public boolean delete(String id)
    {
        collectionDao.deleteCollectionByColId(id);
        return true;
    }

    @Transactional(readOnly = true)
    public ContentEdit getCollectionByColId(String id){return collectionDao.getCollectionByColId(id);}

    @Transactional
    public boolean edit(ContentEdit contentEdit,String id){

        collectionDao.editCollection(contentEdit,id);

        return true;
    }

    @Transactional
    public boolean add(ContentEdit contentEdit, String content) {

        contentEdit.setCreateTime(DateUtils.addHours(new Date(),8));
        int colId = collectionDao.addCollection(contentEdit);
        return colId > 0 ? true : false;
    }

    @Transactional(readOnly = true)
    public List<ContentEditType> getCategoryList(){
        return categoryDao.getAll();
    }


    @Transactional(readOnly = true)
    public List<ContentEditType> getAllCategoryList(){
        return categoryDao.getAll();
    }


//    @Transactional(readOnly = true)
//    public List<Category> getAllNewsCategoryList(){
//        return categoryDao.getAllNews();
//    }


    @Transactional
    public boolean addCategory(ContentEditType contentEditType){
        int count = categoryDao.add(contentEditType);
        if (count <= 0){
            return false;
        }
        return true;
    }


    @Transactional
    public boolean updateCategory(ContentEditType contentEditType){
        int count = categoryDao.update(contentEditType);
        if (count <= 0){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteCategory(String id){
        int count = categoryDao.delete(id);
        if (count <=0 ) {
            return false;
        }
        return true;
    }


    @Transactional(readOnly = true)
    public ContentEditType getCategoryById(String id){
        return categoryDao.get(id);
    }

//    @Transactional(readOnly = true)
//    public Collection getCategoryByTitleAndTime(String colTitle,  String colTime){
//        return collectionDao.getCollectionByTitleAndTime(colTitle,colTime);
//    }

    //byCql
    //获取最新的5个新闻
    @Transactional(readOnly = true)
    public List<ContentEdit> getTop5NewsListByField(){
        return collectionDao.getTop5NewsListByField();
    }

}