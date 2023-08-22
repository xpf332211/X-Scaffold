package com.meiya.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author xiaopengfei
 */
public class ExportWordUtil {

    private static Configuration configuration;
    private static final String SUFFIX = ".docx";

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(ExportWordUtil.class, "/templates");
    }

    /**
     * 生成 Word 文档文件，并通过 HTTP 响应的方式将文件以附件形式提供下载给客户端
     * @param map 数据
     * @param title 标题
     * @param ftlName 模板名称
     */
    public static void exportWord(Map<String, ?> map, String title, String ftlName) throws Exception {
        Template template = configuration.getTemplate(ftlName);
        File file = createDocFile(map,template);
        HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
        String fileName = title + SUFFIX;
        response.setCharacterEncoding("UTF-8");
        //表示响应的内容是Microsoft Word文档
        response.setContentType("application/msword");
        //响应内容作为附件下载
        response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
        try (InputStream is = Files.newInputStream(file.toPath());
             ServletOutputStream sos = response.getOutputStream()){
            //读取输入流数据并写入输出流
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = is.read(buffer)) != -1){
                sos.write(buffer,0,read);
            }
            file.delete();
        }




    }

    /**
     * 根据 map和模板文件生成文档
     * @param dataMap 参数
     * @param template 模板
     * @return 文档
     */
    private static File createDocFile(Map<String, ?> dataMap,Template template) throws Exception {
        File file = File.createTempFile("init",SUFFIX);
        Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8);
        template.process(dataMap,writer);
        writer.close();
        return file;
    }
}
