package com.github.angdx.gs.voter;

import com.github.angdx.gs.configattr.IgnoreVerifierConfigAttribute;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * 检验忽略投票者，通过{@link IgnoreVerifierConfigAttribute} 投出票
 *
 * @author 王东旭
 */
public class IgnoreVerifierVoter implements RBACAccessDecisionVoter<FilterInvocation> {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getClass().isAssignableFrom(IgnoreVerifierConfigAttribute.class);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                switch (((IgnoreVerifierConfigAttribute) attribute).isIgnore()) {
                    case NO:
                        return ACCESS_DENIED;
                    case YES:
                        return ACCESS_GRANTED;
                    case ABANDON:
                        return ACCESS_ABSTAIN;
                    default:
                        return ACCESS_ABSTAIN;
                }
            }
        }
        return ACCESS_ABSTAIN;
    }

    @Override
    public boolean passGreenChannel() {
        return true;
    }

    @Override
    public boolean denyGreenChannel() {
        return false;
    }
}
