package com.tuniondata.jtserver.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Think on 2017/10/19.
 */
public class PropertyReader {
    private static ResourceBundle resource = ResourceBundle.getBundle("application");

    private static Map<String,String> mapProp=new HashMap<String,String>();

    /**
     * 获取application.properties文件属性值
     *
     * @param key
     * @return
     */
    public static String getProperties(String key) {
        try {
            if (key != null && !"".equals(key)) {
                if(!mapProp.containsKey(key)){
                    mapProp.put(key, resource.getString(key));
                }
                return mapProp.get(key);
            } else {
                return "";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取application.properties文件
     *
     * @param fileName
     * @return
     */
    public static Properties getProperties(String fileName, boolean flag) {
        Properties props = new Properties();
        try {
            props.load(PropertyReader.class.getClassLoader().getResourceAsStream(fileName));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return props;
    }

}
