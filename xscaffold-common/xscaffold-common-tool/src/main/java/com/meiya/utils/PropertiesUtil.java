package com.meiya.utils;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaopf
 */
public class PropertiesUtil {
    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<>();
    private static final Map<String,Long> MODIFY_TIME_MAP = new ConcurrentHashMap<>();
    @Setter
    private String configPath = "";
    private static final String FILE_SUFFIX = ".properties";

    /**
     * 单例
     */
    private PropertiesUtil(){}
    private static class SingleHolder{
        private static final PropertiesUtil INSTANCE = new PropertiesUtil();
    }
    public static PropertiesUtil getInstance(){
        return SingleHolder.INSTANCE;
    }

    /**
     * 根据文件名和properties的key获取properties的value
     * @param fileName 文件名
     * @param propertiesKey properties的key
     * @return properties的value
     */

    public String getPropertiesValue(String fileName,String propertiesKey) throws IOException, URISyntaxException {
        //截取文件名
        fileName = convertFileName(fileName);
        if (PROPERTIES_MAP.get(fileName) == null){
            //将当前properties文件加载进properties，并修改缓存
            loadProperties(fileName);
        }else {
            //检查是否有更新文件，有则重新加载properties文件进properties 并修改缓存
            checkPropertiesModified(fileName);
        }
        return PROPERTIES_MAP.get(fileName).getProperty(propertiesKey);
    }

    /**
     * 检查是否有更新文件 若有则重新调用loadProperties方法
     * @param fileName 文件名
     */
    private void checkPropertiesModified(String fileName) throws URISyntaxException, IOException {
        //根据文件名获取文件对象
        File file = getPropertiesFile(fileName);
        //获取文件最后修改时间
        long lastModified = file.lastModified();
        //获取缓存中较早的修改时间
        Long earlyModified = MODIFY_TIME_MAP.get(fileName);
        //文件有更新 重新加载properties 并修改缓存
        if (lastModified > earlyModified){
            loadProperties(fileName);
        }
    }

    /**
     * 将最新文件加载到properties中 并修改缓存
     * @param fileName 文件名
     */
    private void loadProperties(String fileName) throws IOException, URISyntaxException {
        //根据文件名获取文件对象
        File file = getPropertiesFile(fileName);
        //获取文件最后修改时间
        Long lastModified = file.lastModified();
        //防止缓存可能已经被更改
        if (PROPERTIES_MAP.get(fileName) != null){
            PROPERTIES_MAP.remove(fileName);
        }
        //将最新文件加载到properties中
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream)){
            properties.load(reader);
        }
        //修改缓存
        PROPERTIES_MAP.put(fileName,properties);
        MODIFY_TIME_MAP.put(fileName,lastModified);

    }

    /**
     * 根据文件名获取文件对象
     * @param fileName 文件名
     * @return 文件对象
     */
    private File getPropertiesFile(String fileName) throws URISyntaxException, FileNotFoundException {
        File file = null;
        //配置了configPath
        if (this.configPath !=null && StringUtils.isNoneBlank(this.configPath)){
            file = new File(this.configPath + File.separator + fileName + FILE_SUFFIX);
        }
        if (file != null && file.exists()){
            return file;
        }
        //根据当前java进程的工作路径获取文件
        file = new File(System.getProperty("user.dir") + File.separator + fileName + FILE_SUFFIX);
        if (file.exists()){
            return file;
        }
        //构建出相对于classpath根目录的文件路径
        URL url = PropertiesUtil.class.getResource("/" + fileName + FILE_SUFFIX);
        if (url != null){
            return new File(url.toURI());
        }
        throw new FileNotFoundException("未找到文件名为" + fileName + "的文件路径");
    }


    /**
     * 截取掉文件名”.properties“后缀
     * @param fileName 文件名
     * @return 截取后的文件名
     */
    private static String convertFileName(String fileName) {
        if (fileName.endsWith(FILE_SUFFIX)){
            int index = fileName.lastIndexOf(".");
            fileName = fileName.substring(0, index);
        }
        return fileName;
    }


}
