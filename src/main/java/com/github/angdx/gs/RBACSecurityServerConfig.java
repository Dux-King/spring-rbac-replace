package com.github.angdx.gs;

import com.github.angdx.gs.server.ConfigAttributeExtractor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 服务端配置类
 *
 * @author 王东旭
 */
@Configuration
public class RBACSecurityServerConfig {

    private ConfigAttributeExtractor configAttributeExtractor;

    public RBACSecurityServerConfig(ConfigAttributeExtractor configAttributeExtractor) {
        this.configAttributeExtractor = configAttributeExtractor;
    }

    public Collection<ConfigAttribute> getConfigAttributes(String url, HttpMethod httpMethod, String appName){

        List<ConfigAttribute> list = new ArrayList<>();
        Collection<ConfigAttribute> configAttributes = configAttributeExtractor.supplementConfigAttributes();
        Collection<ConfigAttribute> grantedAuthorities = configAttributeExtractor
                .obtainConfigAttributes(url, httpMethod, appName);
        if (configAttributes != null){
            list.addAll(configAttributes);
        }
        if (grantedAuthorities != null){
            list.addAll(grantedAuthorities);
        }
        return list;
    }
}
