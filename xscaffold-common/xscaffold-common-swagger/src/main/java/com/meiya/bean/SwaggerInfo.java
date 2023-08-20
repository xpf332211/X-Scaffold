package com.meiya.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaopf
 */
@Component
@ConfigurationProperties(prefix = "xscaffold.swagger")
public class SwaggerInfo {
    /**
     * 扫描包路径
     */
    private String basePackage;
    /**
     * 标题
     */
    private String title;
    /**
     * 姓名
     */
    private String contactName;
    /**
     * 地址
     */
    private String contactUrl;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 版本
     */
    private String version;
    /**
     * 描述
     */
    private String description;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
