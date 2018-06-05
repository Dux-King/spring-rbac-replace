package com.github.angdx.gs.store.client;

import com.github.angdx.gs.store.RBACValidationStore;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;

import java.util.List;

/**
 * 校验请求接口
 *
 * @author 王东旭
 */
public interface RBACRequestVerifierClient extends RBACValidationStore {

    /**
     * 获取权限配置属性
     * @param url 需要校验的url
     * @return url需要的安全attribute
     */
    List<ConfigAttribute> getConfigAttribute(String url, HttpMethod httpMethod);
}
