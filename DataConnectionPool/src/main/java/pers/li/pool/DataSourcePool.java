package pers.li.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * 数据库连接池的实现类--连接池单例
 * create by lishengbo 2018/6/10
 */
public class DataSourcePool extends AbstractDataSourcePool {

    //创建锁
    private Object lock=new Object();

    private static DataSourcePool dataSourcePool=new DataSourcePool();

    private DataSourcePool() {
        //定义初始化连接池的信息的方法
//        initPool();
        DataSourceConfig.initPool();
    }
    public static DataSourcePool getInstance(){
        return dataSourcePool;
    }

//    private void initPool() {
//
//    }

    @Override
    public ConnectionPool getConnection() {
        ConnectionPool connectionPool=null;
        synchronized (lock){
            //判断连接池中没有连接
            if(DataSourceConfig.connectionPools.size()==0){
                //初始化连接池中连接数
                DataSourceConfig.createConnections(DataSourceConfig.initSize);
            }
            //获取到连接池中空闲的连接对象
            connectionPool=DataSourceConfig.getRealConnection();
            //连接池中没有空闲连接。轮询查找
//            if(connectionPool==null){
//                //轮询获取连接池中对象
//                while (true){
//                    connectionPool=DataSourceConfig.getRealConnection();
//                    if(connectionPool!=null){
//                        return connectionPool;
//                    }
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(30);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
            //优化如下
                //轮询获取连接池中对象
                while (connectionPool==null){
                    //没有空闲连接时，创建新的连接--
                    DataSourceConfig.createConnections(DataSourceConfig.incrSize);
                    connectionPool=DataSourceConfig.getRealConnection();
                    try {
                        TimeUnit.MILLISECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
        }

        return connectionPool;
    }



}
