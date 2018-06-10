package pers.li.pool;

import pers.li.utils.PropertiesUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * create by lishengbo 2018/6/10
 * 数据库连接的初始化配置
 *
 */
public class DataSourceConfig {

    //保存连接池中连接对象中的集合--线程安全集合
    //    public static Vector<ConnectionPool> connectionPools=new Vector<>();
    //也可以使用ArrayList--非线程安全--或者转为线程安全
    public static List<ConnectionPool> connectionPools=
            Collections.synchronizedList(new ArrayList<ConnectionPool>());
    //--------------------------------------
    //连接池属性定义
    //初始连接数
    public static int initSize=2;
    //最大连接数
    private static int maxSize=10;
    //每次增加连接数
    public static int incrSize=5;
    //超时时间
    private static int timeOut=1000;

    //完成任务共创建了多少个连接
    public  static  int num =0;

    //---------------------------------------
    //连接池连接信息
    private static String driverClassName;
    private static String url;
    private static String userName;
    private static String passWord;

    //初始化连接--
    public static void initPool() {
        driverClassName= PropertiesUtils.getStaticProperty("jdbc.driver.class");
        url=PropertiesUtils.getStaticProperty("jdbc.url");
        userName=PropertiesUtils.getStaticProperty("jdbc.userName");
        passWord=PropertiesUtils.getStaticProperty("jdbc.passWord");

        initSize=getPararm("initSize",initSize);
        maxSize=getPararm("maxSize",maxSize);
        incrSize=getPararm("incrSize",incrSize);
        timeOut=getPararm("timeOut",timeOut);

        //将驱动注册到对应的DriverManager中
        try {
            //使用以下驱动也可以注册
//            new com.mysql.jdbc.Driver();
            Driver driver=(Driver) Class.forName(driverClassName).newInstance();
            DriverManager.registerDriver(driver);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //添加连接对象到连接池中的方法
    public static void createConnections(int count){
        //判断连接池中连接数+每次增加的连接数<=最大连接数
        if(connectionPools.size()+incrSize<=maxSize){
            createRealConnection(count);
        }else if(connectionPools.size()+incrSize>maxSize){
            count=maxSize-connectionPools.size();
            createRealConnection(count);
        }else{
            System.out.println("连接池连接已达上限------");
        }
    }

    private static void createRealConnection(int count) {
        for (int i = 0; i <count ; i++) {
            num++;
            System.out.println("初始化"+num+"个连接对象====");
            //创建连接对象
            try {
                Connection connection = DriverManager.getConnection(url, userName, passWord);
                //封装为包装后的连接对象
                ConnectionPool connectionPool=new ConnectionPool(connection,false);
                //放入连接池
                connectionPools.add(connectionPool);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    ;


    private static int getPararm(String key,int s){
        return inte(getValue(key)==null?s+"":getValue(key));
    }

    public static  String getValue(String key){
       return PropertiesUtils.getStaticProperty(key);
    }

    public static int inte(String s){
        Integer integer = Integer.valueOf(s);
        return integer;
    };

    public static void main(String[] args) {
        System.out.println(getPararm("incrSiz1e",incrSize));
    }


    /**
     * 删选连接池中空闲的连接对象，看连接对象是否可用--可用缓存优化连接池
     * @return 10000并发
     */
    public static ConnectionPool getRealConnection() {
        //针对list而言forEach性能低于fori，故使用fori
        for (int i = 0; i < connectionPools.size(); i++) {
            //获取连接池中连接对象
            ConnectionPool connectionPool = connectionPools.get(i);
            //判断是否为空闲连接-是否被占用
            if(!connectionPool.isUsed()){
                //审核连接对象是否与数据库能够成功连接==>超时时间
                //获取java.sql.connection对象
                Connection connection = connectionPool.getConnection();
                try {
                    //如果连接不可用--创建新连接补充
                    if(!connection.isValid(timeOut)){
                        connection=DriverManager.getConnection(url,userName,passWord);
                        //补齐坏连接--坏连接被gc
                        connectionPool.setConnection(connection);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //设置连接为占用状态
                connectionPool.setUsed(true);
                return connectionPool;
            }
        }
        return null;
    }
}
