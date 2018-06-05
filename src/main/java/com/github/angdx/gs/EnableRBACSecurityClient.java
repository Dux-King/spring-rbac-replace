package com.github.angdx.gs;

import com.github.angdx.gs.store.client.UaaRBACRequestVerifierClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * RBAC 校验客户端使能，同时需要继承{@link RBACSecurityClientConfig}实现对应抽象方法，
 * 默认使用{@link UaaRBACRequestVerifierClient}作为client
 * 如果需要更改server地址和属性需要配置文件中配置rbacSecurity.baseUrl和其他相关属性{@link RBACSecurityProperties}
 *
 * @author 王东旭
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RBACSecurityClientConfig.class,RBACSecurityProperties.class})
public @interface EnableRBACSecurityClient {
}
