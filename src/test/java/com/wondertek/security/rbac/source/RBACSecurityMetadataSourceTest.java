package com.migu.security.rbac.source;

import com.github.angdx.gs.configattr.GrantAttribute;
import com.github.angdx.gs.configattr.IgnoreVerifierConfigAttribute;
import com.github.angdx.gs.configattr.RoleGrantAttribute;
import com.github.angdx.gs.source.RBACIgnoreVerifierProcessor;
import com.github.angdx.gs.source.RBACSecurityMetadataSource;
import com.github.angdx.gs.store.RBACValidationStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RBACSecurityMetadataSourceTest {

    private RBACSecurityMetadataSource rbacSecurityMetadataSource;

    @Mock
    private RBACIgnoreVerifierProcessor rbacIgnoreVerifierProcessor;

    @Mock
    private RBACValidationStore rbacValidationStore;

    private GrantAttribute roleGrantAttribute;

    @Before
    public void setup() {
        roleGrantAttribute = new RoleGrantAttribute("test");
        when(rbacValidationStore.getConfigAttribute(anyString(),any()))
                .thenReturn(Collections.singletonList(roleGrantAttribute));
        rbacSecurityMetadataSource = new RBACSecurityMetadataSource(rbacValidationStore,rbacIgnoreVerifierProcessor);
    }
    @Test
    public void ignoreVerifierGetAttributes() throws Exception {
        when(rbacIgnoreVerifierProcessor.isIgnoreVerifier(any())).thenReturn(true);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        FilterInvocation filterInvocation = new FilterInvocation(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        assertThat(rbacSecurityMetadataSource.getAttributes(filterInvocation))
                .contains(new IgnoreVerifierConfigAttribute(IgnoreVerifierConfigAttribute.IsIgnore.YES));
    }
    @Test
    public void noLoginVerifierGetAttributes() throws Exception {
        when(rbacIgnoreVerifierProcessor.isIgnoreVerifier(any())).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("test",
                "test", Collections.singletonList(new RoleGrantAttribute("test"))));
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        FilterInvocation filterInvocation = new FilterInvocation(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        assertThat(rbacSecurityMetadataSource.getAttributes(filterInvocation))
                .contains(new IgnoreVerifierConfigAttribute(IgnoreVerifierConfigAttribute.IsIgnore.NO));
    }
    @Test
    public void storeVerifierGetAttributes() throws Exception {
        when(rbacIgnoreVerifierProcessor.isIgnoreVerifier(any())).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test",
                "test", Collections.singletonList(new RoleGrantAttribute("test"))));
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        FilterInvocation filterInvocation = new FilterInvocation(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        assertThat(rbacSecurityMetadataSource.getAttributes(filterInvocation))
                .contains(new IgnoreVerifierConfigAttribute(IgnoreVerifierConfigAttribute.IsIgnore.ABANDON),roleGrantAttribute);
    }
}