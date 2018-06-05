package com.github.angdx.gs.configattr;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

/**
 * 授权对象{@link GrantedAuthority}和校验授权配置对象{@link ConfigAttribute}
 * 使用{@link GrantedAuthority}授权，使用{@link ConfigAttribute}鉴权
 *
 * @author 王东旭
 */
public interface GrantAttribute extends GrantedAuthority,ConfigAttribute {
}
