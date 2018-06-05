package com.github.angdx.gs.server;

import com.github.angdx.gs.RBACSecurityServerConfig;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * server端GrantedAuthority获取器
 * @author 王东旭
 */
public interface ConfigAttributeExtractor {
    /**
     * 作为服务端必须实现的一个接口，注入spring ioc，他被注入到{@link RBACSecurityServerConfig}
     * @param url url
     * @param httpMethod httpMethod
     * @param appName 服务名
     * @return GrantedAuthority 集合
     */
    Collection<ConfigAttribute> obtainConfigAttributes(String url, HttpMethod httpMethod, String appName);


    /**
     * 补充{@link #obtainConfigAttributes},可选
     * @return Collection<ConfigAttribute>
     */
    default Collection<ConfigAttribute> supplementConfigAttributes() {
        return Collections.emptyList();
    }

}
