package com.github.angdx.gs.store;

import com.github.angdx.gs.store.client.RBACRequestVerifierClient;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;

import java.util.List;

/**
 * 简单实现,直接请求uaa
 */
public class RemoteRBACValidationStore implements RBACValidationStore {

    private RBACRequestVerifierClient rbacRequestVerifierClient;

    public RemoteRBACValidationStore(RBACRequestVerifierClient rbacRequestVerifierClient) {
        this.rbacRequestVerifierClient = rbacRequestVerifierClient;
    }

    @Override
    public List<ConfigAttribute> getConfigAttribute(String url, HttpMethod httpMethod) {
        return rbacRequestVerifierClient.getConfigAttribute(url,httpMethod);
    }
}
