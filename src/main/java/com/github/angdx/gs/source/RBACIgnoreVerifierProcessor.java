package com.github.angdx.gs.source;

import org.springframework.security.web.FilterInvocation;

/**
 * 忽略校验资格判断器
 * @author 王东旭
 */
public interface RBACIgnoreVerifierProcessor {
    /**
     *
     * @param fi 待校验对象
     * @return 是否校验结果
     */
    boolean isIgnoreVerifier(FilterInvocation fi);
}
