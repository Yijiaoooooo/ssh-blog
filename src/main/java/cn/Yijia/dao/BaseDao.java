package cn.Yijia.dao;

import cn.Yijia.domain.Page;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class BaseDao<T> {
    private Class<T> entityClass;

    private HibernateTemplate hibernateTemplate;

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    public Session getSession() {
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }

    public BaseDao() {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        entityClass = (Class<T>)params[0];
    }

    public T load(Serializable id) {
        return hibernateTemplate.load(entityClass, id);
    }

    public T get(Serializable id) {
        return hibernateTemplate.get(entityClass, id);
    }

    public List<T> loadAll() {
        return hibernateTemplate.loadAll(entityClass);
    }

    public void remove(T entity) {
        hibernateTemplate.delete(entity);
    }

    public void removeAll(String tableName) {
        getSession().createSQLQuery("truncate table" + tableName + "").executeUpdate();
    }

    public void update(T entity) {
        hibernateTemplate.update(entity);
    }

    public List find(String hql) {
        return hibernateTemplate.find(hql);
    }

    public void save(T entity) {
        hibernateTemplate.save(entity);
    }

    /**
     * 带参hql查询
     * @param hql
     * @param objects
     * @return
     */
    public List find(String hql, Object... objects) {
        return hibernateTemplate.find(hql, objects);
    }

    /**
     * 对延迟加载的实体PO执行初始化
     * @param entity
     */
    public void initialize(Object entity) {
        hibernateTemplate.initialize(entity);
    }

    private static  String removeSelect(String hql) {
        Assert.hasText(hql);
        int begin = hql.toLowerCase().indexOf("from");
        Assert.isTrue(begin != -1, "hql:" + hql + "must have a from");
        return hql.substring(begin);
    }

    private static String removeOrders(String hql) {
        Assert.hasText(hql);
        Pattern p = compile("order\\s*by[\\w|\\W|\\s|\\S]*", CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 分页查询函数，使用hql.
     *
     * @param pageNo 页号,从1开始.
     */
    public Page pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
        Assert.hasText(hql);
        Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
        // Count查询
        String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
        List countlist = getHibernateTemplate().find(countQueryString, values);
        long totalCount = (Long) countlist.get(0);

        if (totalCount < 1) {
            return new Page();
        }
        // 实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);
        Query query = createQuery(hql, values);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

        return new Page(startIndex, totalCount, pageSize, list);
    }

    /**
     * 创建Query对象. 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
     * 留意可以连续设置,如下：
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 调用方式如下：
     * <pre>
     *        dao.createQuery(hql)
     *        dao.createQuery(hql,arg0);
     *        dao.createQuery(hql,arg0,arg1);
     *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
     * </pre>
     *
     * @param values 可变参数.
     */
    public Query createQuery(String hql, Object... values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query;
    }

}
