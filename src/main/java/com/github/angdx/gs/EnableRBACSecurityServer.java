package com.github.angdx.gs;

import com.github.angdx.gs.server.ConfigAttributeExtractor;
import com.github.angdx.gs.server.RBACVerifyEndpoint;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 使能server端,需要实现{@link ConfigAttributeExtractor}注入spring ioc
 * 被{@link RBACSecurityServerConfig} 使用
 *
 * @author 王东旭
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RBACVerifyEndpoint.class,RBACSecurityServerConfig.class})
public @interface EnableRBACSecurityServer {
}
