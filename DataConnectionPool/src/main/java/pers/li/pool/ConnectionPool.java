package pers.li.pool;

import java.sql.Connection;

/**
 * 连接类--新增连接是否被使用的状态
 * create by lishengbo 2018/6/10
 */
public class ConnectionPool {


    //数据库的连接对象
    private Connection connection;
    //连接是否被使用的标记状态
    private boolean isUsed;

    public ConnectionPool(Connection connection,boolean isUsed){
        this.connection=connection;
        this.isUsed=isUsed;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    //设置连接池为空闲状态的方法
    public void close(){
        System.out.println("释放连接....."+this);
        this.isUsed=false;
    }

}
