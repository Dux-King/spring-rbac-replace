package com.github.angdx.gs.voter;

import org.springframework.security.access.AccessDecisionVoter;

/**
 * RBAC 投票者接口，实现后注入spring ioc 容器即可参与投票
 * @param <S> 被选举者类型
 *
 * @author 王东旭
 */
public interface RBACAccessDecisionVoter<S> extends AccessDecisionVoter<S> {

    /**
     * 一票通过的权力
     * @return 是否是一票通过投票
     */
    boolean passGreenChannel();
    /**
     * 一票否决的权力
     * @return 是否是一票否决投票
     */
    boolean denyGreenChannel();
}
