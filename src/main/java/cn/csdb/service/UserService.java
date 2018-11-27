package cn.csdb.service;

import cn.csdb.model.User;
import cn.csdb.repository.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by huangwei on 2018/5/7.
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public boolean isAdmin(String loginid){
        return userDao.isAdmin(loginid);
    }

    public User getUserById(String loginid){
        return userDao.getUserById(loginid);
    }

    public void addGuestUser(String loginId,String trueName){
        userDao.addGuestUser(loginId,trueName);
    }
}
