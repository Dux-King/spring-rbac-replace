package com.github.angdx.gs.source;

import com.github.angdx.gs.RBACSecurityProperties;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

/**
 * yml配置实现校验忽略
 * @author 王东旭
 */
public class YmlIgnoreVerifierProcessor implements RBACIgnoreVerifierProcessor {
    private RBACSecurityProperties rbacSecurityProperties;
    private static AntPathMatcher antPathMatcher= new AntPathMatcher();

    public YmlIgnoreVerifierProcessor(RBACSecurityProperties rbacSecurityProperties) {
        this.rbacSecurityProperties = rbacSecurityProperties;
    }

    @Override
    public boolean isIgnoreVerifier(FilterInvocation fi) {
        String url = fi.getRequestUrl().split("\\?")[0];
        if (rbacSecurityProperties.getIgnoreUrl()==null) return false;
        //放行检验端点
        if (antPathMatcher.match(rbacSecurityProperties.getEndpoint(),url)){
            return true;
        }
        for (String ignoreUrl : rbacSecurityProperties.getIgnoreUrl()){
            if (antPathMatcher.match(ignoreUrl,url)){
                return true;
            }
        }
        return false;
    }
}
