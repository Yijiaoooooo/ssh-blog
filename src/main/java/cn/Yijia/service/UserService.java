package cn.Yijia.service;

import cn.Yijia.dao.LoginLogDao;
import cn.Yijia.dao.UserDao;
import cn.Yijia.domain.LoginLog;
import cn.Yijia.domain.User;
import cn.Yijia.exception.UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginLogDao loginLogDao;

    public void register(User user) throws UserExistException {
        User u = userDao.getUserByUserName(user.getUserName());

        if(null == u) {
            throw new UserExistException("用户已经存在！");
        } else {
            user.setCredit(100);
            user.setUserType(1);

            userDao.save(user);
        }
    }

    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    public User getUserById(int userId) {
        return userDao.get(userId);
    }

    public void lockUser(String userName) {
        User u = userDao.getUserByUserName(userName);
        u.setLocked(User.USER_LOCK);

        userDao.update(u);
    }

    public void unLockUser(String userName) {
        User u = userDao.getUserByUserName(userName);
        u.setLocked(User.USER_UNLOCK);

        userDao.update(u);
    }

    private void setLoginLog(User user) {
        LoginLog loginLog = new LoginLog();
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(new Date());
        loginLog.setUser(user);

        loginLogDao.save(loginLog);
    }

    public void loginSuccess(User user) {
        setLoginLog(user);
        userDao.get(user.getUserId()).setCredit(user.getCredit() + 5);
    }

}
