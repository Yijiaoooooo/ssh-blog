package cn.Yijia.service;

import cn.Yijia.dao.BoardDao;
import cn.Yijia.dao.PostDao;
import cn.Yijia.dao.TopicDao;
import cn.Yijia.dao.UserDao;
import cn.Yijia.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ForumService {
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BoardDao boardDao;
    @Autowired
    private PostDao postDao;

    public void addTopic(Topic topic) {
        User user = userDao.get(topic.getUserId());
        user.setCredit(user.getCredit() + 10);

        Board board = boardDao.get(topic.getBoardId());
        board.setTopicNum(board.getTopicNum() + 1);

        topicDao.save(topic);
        userDao.save(user);
        boardDao.save(board);

        MainPost mainPost = topic.getMainPost();
        mainPost.setTopic(topic);
        mainPost.setCreateTime(new Date());
        mainPost.setUser(user);
        mainPost.setBoardId(board.getBoardId());
        mainPost.setPostTitle(topic.getTopicTitle());

        postDao.save(mainPost);
    }

    public void deleteTopic(int topicId) {
        Topic topic = topicDao.get(topicId);

        Board board = boardDao.get(topic.getBoardId());
        board.setTopicNum(board.getTopicNum() - 1);

        User user = userDao.get(topic.getUserId());
        user.setCredit(user.getCredit() - 50);

        topicDao.remove(topic);
        postDao.DeleteTopicPosts(topicId);

        //userDao.save(user);
        //boardDao.save(board);
    }

    public void addPost(Post post) {
        postDao.save(post);

        User user = post.getUser();
        user.setCredit(user.getCredit() + 5);

        Topic topic = topicDao.get(post.getTopic().getTopicId());
        topic.setReplies(topic.getReplies() + 1);
        topic.setLastPost(new Date());

        userDao.update(user);

    }
}
