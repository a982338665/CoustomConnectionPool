package pers.li.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * create by lishengbo 2018/6/10
 * 读取配置文件信息
 */
public class PropertiesUtils extends Properties{

    private static PropertiesUtils utils=new PropertiesUtils();

    private PropertiesUtils() {
        try (
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("pool.properties");
                ){
            this.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStaticProperty(String key){
        return (String) utils.get(key);
    }

    public static void main(String[] args) {
//        String property = new PropertiesUtils().getProperty("jdbc.url");
//        System.out.println(property);
        System.out.println(PropertiesUtils.getStaticProperty("jdbc.url1"));
    }
}
