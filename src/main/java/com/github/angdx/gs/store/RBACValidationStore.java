package com.github.angdx.gs.store;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;

import java.util.List;

/**
 * 权限仓库
 */
public interface RBACValidationStore {

    /**
     * 通过请求url和方法获取拥有的{@link ConfigAttribute}
     * @param url url
     * @param httpMethod httpMethod
     * @return {@link ConfigAttribute} 集合
     */
    List<ConfigAttribute> getConfigAttribute(String url, HttpMethod httpMethod);
}
