package pers.li.pool;

/**
 * create by lishengbo 2018/6/10
 *  抽象连接池定义
 */
public abstract class AbstractDataSourcePool {

    /**
     * 定义一个获取连接池中连接的方法
     */
    public  abstract ConnectionPool getConnection();
}
