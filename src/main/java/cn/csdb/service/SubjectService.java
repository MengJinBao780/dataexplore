package cn.csdb.service;

import cn.csdb.model.Subject;
import cn.csdb.repository.SubjectDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: DataSync
 * @description: Subject Service class
 * @author: xiajl
 * @create: 2018-10-22 15:53
 **/
@Service
public class SubjectService {
    @Resource
    private SubjectDao subjectDao;

    /**
     * Function Description: 根据subjectCode查找对象
     *
     * @param:  subjectCode:专题库代码
     * @return: Sujbect对像
     * @auther: xiajl
     * @date:   2018/10/22 16:02
     */
    public Subject findBySubjectCode(String subjectCode){
        return subjectDao.findBySubjectCode(subjectCode);
    }

    /**
     * Function Description: 根据用户名查找对象
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/31 9:37
     */
    public Subject findByUser(String userName){
        return subjectDao.findByUser(userName);
    }


    /**
     * Function Description: validate subject code login
     * @param userName
     * @param password
     * @return loginStatus
     */
    public int validateLogin(String userName, String password) {
        int loginStatus = 0;
        loginStatus = subjectDao.validateLogin(userName, password);

        return loginStatus;
    }
}
