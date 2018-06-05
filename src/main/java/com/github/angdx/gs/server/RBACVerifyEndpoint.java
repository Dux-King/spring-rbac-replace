package com.github.angdx.gs.server;

import com.github.angdx.gs.RBACSecurityServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务端校验端点
 *
 * @author 王东旭
 */
@RestController
@RequestMapping("/api")
public class RBACVerifyEndpoint {
    private final Logger log = LoggerFactory.getLogger(RBACVerifyEndpoint.class);

    private RBACSecurityServerConfig rbacSecurityServerConfig;

    public RBACVerifyEndpoint(RBACSecurityServerConfig rbacSecurityServerConfig) {
        this.rbacSecurityServerConfig = rbacSecurityServerConfig;
    }

    @GetMapping("/rbac-verify")
    public ResponseEntity verifier(@RequestParam String url, @RequestParam String method, @RequestParam String appName){
        Collection<ConfigAttribute> authorities = rbacSecurityServerConfig
                .getConfigAttributes(url, HttpMethod.resolve(method), appName);
        if (authorities == null){
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<String> authorityStr = authorities
                .stream()
                .map(ConfigAttribute::getAttribute)
                .collect(Collectors.toList());
        if (log.isDebugEnabled()){
            log.debug("verify endpoint:[url:{},method:{},application name:{}] has {}",url,method,appName,authorities);
        }
        return ResponseEntity.ok(authorityStr);
    }
}
