package cn.Yijia.dao;

import cn.Yijia.BaseTest;
import cn.Yijia.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.testng.Assert.*;

public class UserDaoTest extends BaseTest {

    @Autowired
    public UserDao userDao;
    @Test
    public void testGet() {
        User user = userDao.get(1);
        System.out.println(user);
    }
}