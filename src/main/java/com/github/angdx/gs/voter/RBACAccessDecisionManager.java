package com.github.angdx.gs.voter;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 访问控制决策者
 *
 * @author 王东旭
 */
public class RBACAccessDecisionManager extends AbstractAccessDecisionManager {

    public RBACAccessDecisionManager(List<RBACAccessDecisionVoter> decisionVoters) {
        super(decisionVoters.stream()
            .map(rbacAccessDecisionVoter -> (AccessDecisionVoter<?>) rbacAccessDecisionVoter)
            .collect(Collectors.toList()));
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
        int deny = 0;

        for (AccessDecisionVoter voter : getDecisionVoters()) {
            if (!RBACAccessDecisionVoter.class.isAssignableFrom(voter.getClass())){
                continue;
            }
            RBACAccessDecisionVoter rbacVoter = (RBACAccessDecisionVoter) voter;
            if (rbacVoter.passGreenChannel() &&
                    voter.vote(authentication, object, configAttributes) == AccessDecisionVoter.ACCESS_GRANTED){
                return;
            }
            if (rbacVoter.denyGreenChannel() &&
                    voter.vote(authentication, object, configAttributes) == AccessDecisionVoter.ACCESS_DENIED){
                throw new AccessDeniedException(messages.getMessage(
                        "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
            }
            int result = voter.vote(authentication, object, configAttributes);

            if (logger.isDebugEnabled()) {
                logger.debug("Voter: " + voter + ", returned: " + result);
            }

            switch (result) {
                case AccessDecisionVoter.ACCESS_DENIED:
                    throw new AccessDeniedException(messages.getMessage(
                            "AbstractAccessDecisionManager.accessDenied", "Access is denied"));

                case AccessDecisionVoter.ACCESS_GRANTED:
                    deny++;

                    break;

                default:
                    break;
            }
        }

        if (deny > 0) {
            return;
        }

        // 全部弃权处理
        checkAllowIfAllAbstainDecisions();
    }

}
