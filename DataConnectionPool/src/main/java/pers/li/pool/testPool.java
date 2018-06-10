package pers.li.pool;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

/**
 * create by lishengbo 2018/6/10
 */
public class testPool {

    private static AbstractDataSourcePool pool=DataSourcePool.getInstance();

    private static void selectData(){
        ConnectionPool connection = pool.getConnection();
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try{
            statement=  connection.getConnection().prepareStatement("select * FROM  book");
            resultSet = statement.executeQuery();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(resultSet!=null){
                    resultSet.close();
                }
                if(statement!=null){
                    statement.close();
                }
                //此处的关闭并不是关闭java.sql.connection,而是将此连接标记为空闲状态
                if(connection!=null){
                    //用于测试
//                    Thread.sleep(1000);
                    connection.close();
                }
            }catch(Exception e){

            }finally{

            }


        }
    }


    public static void main(String[] args) {
        //单线程测试
//        long first = new java.util.Date().getTime();
//        for (int i = 0; i <10000 ; i++) {
//            selectData();
//        }
//        long midd = new java.util.Date().getTime();

        //多线程测试
        //开启多少个线程定义
        int num=100;
        final CountDownLatch countDownLatch=new CountDownLatch(num);
        for (int i = 0; i <num; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    selectData();
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("共使用连接个数+=="+DataSourceConfig.num);
    }

}
