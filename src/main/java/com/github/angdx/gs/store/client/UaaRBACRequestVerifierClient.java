package com.github.angdx.gs.store.client;

import com.github.angdx.gs.RBACSecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * uaa 实现{@link RBACRequestVerifierClient}
 * @author 王东旭
 */
public class UaaRBACRequestVerifierClient implements RBACRequestVerifierClient{

    private final Logger log = LoggerFactory.getLogger(UaaRBACRequestVerifierClient.class);

    private final RestTemplate restTemplate;
    private RBACSecurityProperties rbacSecurityProperties;

    public UaaRBACRequestVerifierClient(RBACSecurityProperties rbacSecurityProperties,
                                        RestTemplate restTemplate) {
        this.rbacSecurityProperties = rbacSecurityProperties;
        this.restTemplate = restTemplate;
        if (rbacSecurityProperties.getAppName() == null || rbacSecurityProperties.getAppName().trim().equals("")){
            throw new InvalidClientException("no application name configured in application properties");
        }
    }

    @Override
    public List<ConfigAttribute> getConfigAttribute(String url, HttpMethod httpMethod) {
        List<String> configAttributeStrs = getRoles(url,httpMethod.toString(),rbacSecurityProperties.getAppName());
        String[] securtiyConfigStrs = new String[configAttributeStrs.size()];
        configAttributeStrs.toArray(securtiyConfigStrs);
        if (log.isDebugEnabled()){
            log.debug("request configuration attribute:("+httpMethod+")"+url+"->"+configAttributeStrs);
        }
        return SecurityConfig.createList(securtiyConfigStrs);
    }

    /**
     * uaa请求
     * @param url url
     * @param httpMethod 请求方法
     * @param appName 服务名
     * @return 角色集合
     */
    private List<String> getRoles(String url, String httpMethod,String appName) {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(getUaaVerifierUri(), List.class, url, httpMethod, appName);
        return (List<String>) responseEntity.getBody();
    }
    private String getUaaVerifierUri(){
        return rbacSecurityProperties.getUaaVerifierUri()+"?"+
            rbacSecurityProperties.getParamUrl()+"="+"{url}&"+
            rbacSecurityProperties.getParamMethod()+"="+"{method}&"+
            rbacSecurityProperties.getParamAppName()+"="+"{appName}";
    }
}
