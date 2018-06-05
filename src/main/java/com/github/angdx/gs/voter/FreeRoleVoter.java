package com.github.angdx.gs.voter;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * 无前缀角色投票者，通过角色投票,接口{@link RBACAccessDecisionVoter}的实现
 *
 * @author 王东旭
 */
public class FreeRoleVoter implements RBACAccessDecisionVoter<FilterInvocation> {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getClass().isAssignableFrom(SecurityConfig.class);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object,
                    Collection<ConfigAttribute> attributes) {
        if(authentication == null) {
            return ACCESS_DENIED;
        }
        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }

        return result;
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(
        Authentication authentication) {
        return authentication.getAuthorities();
    }

    @Override
    public boolean passGreenChannel() {
        return false;
    }

    @Override
    public boolean denyGreenChannel() {
        return false;
    }
}
