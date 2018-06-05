package com.github.angdx.gs;

import com.github.angdx.gs.store.RBACValidationStore;
import com.github.angdx.gs.store.RemoteRBACValidationStore;
import com.github.angdx.gs.store.client.RBACRequestVerifierClient;
import com.github.angdx.gs.store.client.UaaRBACRequestVerifierClient;
import com.github.angdx.gs.source.RBACIgnoreVerifierProcessor;
import com.github.angdx.gs.source.RBACSecurityMetadataSource;
import com.github.angdx.gs.source.YmlIgnoreVerifierProcessor;
import com.github.angdx.gs.voter.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Client bean装配class
 *
 * @author 王东旭
 */
@Configuration
public class RBACSecurityClientConfig {
    @Bean
    @ConditionalOnMissingBean
    public RBACSecurityInterceptor rbacSecurityInterceptor(RBACSecurityMetadataSource rbacSecurityMetadataSource,
                                                           RBACAccessDecisionManager rbacAccessDecisionManager){
        RBACSecurityInterceptor rbacSecurityInterceptor = new RBACSecurityInterceptor();
        rbacSecurityInterceptor.setAccessDecisionManager(rbacAccessDecisionManager);
        rbacSecurityInterceptor.setSecurityMetadataSource(rbacSecurityMetadataSource);
        return rbacSecurityInterceptor;
    }

    /**
     * 忽略检验投票器
     * @return RBACAccessDecisionVoter
     */
    @Bean
    public RBACAccessDecisionVoter ignoreVerifierVoter(){
        return new IgnoreVerifierVoter();
    }

    /**
     * 过期投票器
     * @return RBACAccessDecisionVoter
     */
    @Bean
    public RBACAccessDecisionVoter expireDateVoter(){
        return new ExpireDateVoter();
    }

    /**
     * 角色投票器
     * @return RBACAccessDecisionVoter
     */
    @Bean
    public RBACAccessDecisionVoter prefixRoleVoter(){
        return new PrefixRoleVoter();
    }

    /**
     * 通行校验器
     * @param rbacAccessDecisionVoters {@link RBACAccessDecisionVoter}
     * @return RBACAccessDecisionManager {@link RBACAccessDecisionManager}
     */
    @Bean
    @ConditionalOnMissingBean
    public RBACAccessDecisionManager rbacAccessdecisionManager(List<RBACAccessDecisionVoter> rbacAccessDecisionVoters){
        return new RBACAccessDecisionManager(rbacAccessDecisionVoters);
    }

    /**
     * configAttribute 数据源
     * @param rbacValidationStore 权限仓库
     * @param rbacIgnoreVerifierProcessor 检验忽略configAttribute处理器
     * @return RBACSecurityMetadataSource
     */
    @Bean
    @ConditionalOnMissingBean
    public RBACSecurityMetadataSource rbacSecurityMetadataSource(RBACValidationStore rbacValidationStore,
                                                                 RBACIgnoreVerifierProcessor rbacIgnoreVerifierProcessor){
        return new RBACSecurityMetadataSource(rbacValidationStore,rbacIgnoreVerifierProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate loadBalancedRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 忽略url校验器
     * @param rbacSecurityProperties {@link RBACSecurityProperties}
     * @return RBACIgnoreVerifierProcessor
     */
    @Bean
    @ConditionalOnMissingBean
    public RBACIgnoreVerifierProcessor rbacIgnoreVerifierProcessor(RBACSecurityProperties rbacSecurityProperties){
        return new YmlIgnoreVerifierProcessor(rbacSecurityProperties);
    }

    /**
     *  装配RBACValidationStore 权限仓库
     * @param rbacRequestVerifierClient {@link #rbacRequestVerifierClient}
     * @return RBACValidationStore
     */
    @Bean
    @ConditionalOnMissingBean
    public RBACValidationStore rbacValidationStore(RBACRequestVerifierClient rbacRequestVerifierClient){
        return new RemoteRBACValidationStore(rbacRequestVerifierClient);
    }
    @Bean
    @ConditionalOnMissingBean
    public RBACRequestVerifierClient rbacRequestVerifierClient(RBACSecurityProperties rbacSecurityProperties){
        return new UaaRBACRequestVerifierClient(rbacSecurityProperties,loadBalancedRestTemplate());
    }
}
