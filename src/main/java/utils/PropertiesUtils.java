package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static final Properties properties;

    static {
        properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("config.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取配置文件值
     * @param key
     * @return
     */
    public static String get(String key){
        //获取key对应的value值
        return properties.getProperty(key);
    }
}
