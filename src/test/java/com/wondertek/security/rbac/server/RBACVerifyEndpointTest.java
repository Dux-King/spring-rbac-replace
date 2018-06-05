package com.migu.security.rbac.server;

import com.github.angdx.gs.RBACSecurityServerConfig;
import com.github.angdx.gs.configattr.GrantAttribute;
import com.github.angdx.gs.configattr.PasswdDateGrantAttribute;
import com.github.angdx.gs.configattr.RoleGrantAttribute;
import com.github.angdx.gs.server.ConfigAttributeExtractor;
import com.github.angdx.gs.server.RBACVerifyEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class RBACVerifyEndpointTest {

    private MockMvc restMvc;

    @Mock
    private ConfigAttributeExtractor configAttributeExtractor;

    private GrantAttribute roleGrantAttribute;

    private GrantAttribute passwdDateGrantAttribute;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        roleGrantAttribute = new RoleGrantAttribute("test");
        passwdDateGrantAttribute = new PasswdDateGrantAttribute(1);
        when(configAttributeExtractor.obtainConfigAttributes(anyString(),anyObject(),anyString()))
                .thenReturn(Collections.singletonList(roleGrantAttribute));
        when(configAttributeExtractor.supplementConfigAttributes())
                .thenReturn(Collections.singletonList(passwdDateGrantAttribute));
        RBACSecurityServerConfig rbacSecurityServerConfig = new RBACSecurityServerConfig(configAttributeExtractor);
        RBACVerifyEndpoint accountResource = new RBACVerifyEndpoint(rbacSecurityServerConfig);
        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }
    @Test
    public void verifier() throws Exception {
        restMvc.perform(get("/api/rbac-verify")
                .param("url","/test")
                .param("method","GET")
                .param("appName","test")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(containsInAnyOrder(roleGrantAttribute.getAttribute()
                        ,passwdDateGrantAttribute.getAttribute())));
    }

}
