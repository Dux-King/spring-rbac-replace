package com.github.angdx.gs;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * rbac配置实体
 *
 * @author 王东旭
 */
@Configuration
@ConfigurationProperties(prefix = "rbacSecurity", ignoreUnknownFields = true)
public class RBACSecurityProperties implements EnvironmentAware {

    private String baseUrl = "http://localhost"; //client请求server的base地址

    private String endpoint = "/api/rbac-verify"; //client请求server的endpoint

    private String appName; //本服务名称默认使用spirng.application.name

    private String paramUrl = "url"; //访问参数名称

    private String paramMethod = "method";//访问参数名称

    private String paramAppName = "appName";//访问参数名称

    private List<String> ignoreUrl; //检验忽略的url集合
    private Environment environment;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUaaVerifierUri() {
        return baseUrl+endpoint;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getParamUrl() {
        return paramUrl;
    }

    public void setParamUrl(String paramUrl) {
        this.paramUrl = paramUrl;
    }

    public String getParamMethod() {
        return paramMethod;
    }

    public void setParamMethod(String paramMethod) {
        this.paramMethod = paramMethod;
    }

    public String getParamAppName() {
        return paramAppName;
    }

    public void setParamAppName(String paramAppName) {
        this.paramAppName = paramAppName;
    }

    public List<String> getIgnoreUrl() {
        return ignoreUrl;
    }

    public void setIgnoreUrl(List<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        // 设置环境的默认值，但是允许默认值使用轻量绑定
        RelaxedPropertyResolver springPropertyResolver = new RelaxedPropertyResolver(this.environment, "spring.application.");
        String springAppName = springPropertyResolver.getProperty("name");
        if(StringUtils.hasText(springAppName)) {
            setAppName(springAppName);
        }
    }
}
