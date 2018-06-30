package cn.Yijia.dao;

import cn.Yijia.BaseTest;
import cn.Yijia.domain.Page;
import cn.Yijia.domain.Post;
import javafx.geometry.Pos;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import static org.testng.Assert.*;

public class PostDaoTest extends BaseTest {
    @Autowired
    private PostDao postDao;

    @Test
    public void testGetPagedPosts() {
        Page p = postDao.getPagedPosts(1,1,1);
    }

    @Test
    public void testDeleteTopicPosts() {
    }

    @Test
    public void testLoad() {
        Post p = postDao.load(1);
        postDao.initialize("123");
    }

    @Test
    public void testGet() {
        Post p = postDao.get(1);
        Assert.assertNotNull(p);
    }

    @Test
    public void testLoadAll() {
    }

    @Test
    public void testRemove() {
    }

    @Test
    public void testRemoveAll() {
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testFind() {
    }

    @Test
    public void testSave() {
    }

    @Test
    public void testFind1() {
    }

    @Test
    public void testInitialize() {
    }
}