package cn.csdb.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by admin on 2014/6/4.
 */
public class PropertiesUtil {


    /**
     * 向properties文件中更新键值对
     * @param map 新的键值对
     * @return jsonObject,1表示成功，0表示出错
     */
    public static JSONObject addProperties(Map<String,String> map, String propertiesPath){
        FileInputStream fis=null;
        FileOutputStream fos=null;
        JSONObject jsonObject=new JSONObject();
        Properties properties=new Properties();
        try {
            fis=new FileInputStream(propertiesPath);
            properties.load(fis);
            fis.close();
            properties.putAll(map);
            fos=new FileOutputStream(propertiesPath);
            properties.store(fos,"update");
            jsonObject.put("info",1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            jsonObject.put("info",0);
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("info",0);
        }finally {
            try {
                if (fis == null)
                    fis.close();
                if (fos == null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  jsonObject;
    }



    public String readValue(String fileName,String key){
        //request.getSession().getServletContext().getRealPath("")+"/WEB-INF/skin.properties";
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(fileName));
            props.load(in);
            String value = props.getProperty(key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
