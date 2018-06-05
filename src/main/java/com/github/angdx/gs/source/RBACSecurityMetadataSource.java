package com.github.angdx.gs.source;

import com.github.angdx.gs.store.RBACValidationStore;
import com.github.angdx.gs.configattr.IgnoreVerifierConfigAttribute;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 请求安全校验配置获取类 {@link SecurityMetadataSource} 的实现
 * @author 王东旭
 */
public class RBACSecurityMetadataSource implements SecurityMetadataSource {

    private RBACValidationStore rbacValidationStore;
    private RBACIgnoreVerifierProcessor rbacIgnoreVerifierProcessor;

    public RBACSecurityMetadataSource(RBACValidationStore rbacValidationStore
            ,RBACIgnoreVerifierProcessor rbacIgnoreVerifierProcessor) {
        this.rbacValidationStore = rbacValidationStore;
        this.rbacIgnoreVerifierProcessor = rbacIgnoreVerifierProcessor;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        if(!supports(object.getClass())) {
            return new ArrayList<>();
        }
        FilterInvocation fi = (FilterInvocation) object;
        //添加忽略校验配置
        if (rbacIgnoreVerifierProcessor.isIgnoreVerifier(fi)){
            return Collections.singletonList(new IgnoreVerifierConfigAttribute(
                    IgnoreVerifierConfigAttribute.IsIgnore.YES));
        }
        if (SecurityContextHolder.getContext().getAuthentication().getClass().isAssignableFrom(
                AnonymousAuthenticationToken.class)){
            return Collections.singletonList(new IgnoreVerifierConfigAttribute(
                    IgnoreVerifierConfigAttribute.IsIgnore.NO));
        }
        List<ConfigAttribute> result = new ArrayList<>();
        List<ConfigAttribute> configAttribute = rbacValidationStore
                .getConfigAttribute(fi.getRequestUrl().split("\\?")[0]
                , HttpMethod.resolve(fi.getRequest().getMethod()));
        if (configAttribute != null) result.addAll(configAttribute);
        result.add(new IgnoreVerifierConfigAttribute(IgnoreVerifierConfigAttribute.IsIgnore.ABANDON));
        return result;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FilterInvocation.class);
    }
}
