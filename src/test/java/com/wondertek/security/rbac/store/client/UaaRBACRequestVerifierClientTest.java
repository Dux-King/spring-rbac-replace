package com.migu.security.rbac.store.client;

import com.github.angdx.gs.RBACSecurityProperties;
import com.github.angdx.gs.configattr.GrantAttribute;
import com.github.angdx.gs.configattr.PasswdDateGrantAttribute;
import com.github.angdx.gs.configattr.RoleGrantAttribute;
import com.github.angdx.gs.store.client.RBACRequestVerifierClient;
import com.github.angdx.gs.store.client.UaaRBACRequestVerifierClient;
import com.migu.security.rbac.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.MockRestServiceServer.createServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@Import(RBACSecurityProperties.class)
public class UaaRBACRequestVerifierClientTest {

    private RBACRequestVerifierClient rbacRequestVerifierClient;

    @Mock
    private RBACSecurityProperties rbacSecurityProperties;

    private GrantAttribute roleGrantAttribute;

    private GrantAttribute passwdDateGrantAttribute;

    @Before
    public void setup() throws IOException {
        when(rbacSecurityProperties.getUaaVerifierUri()).thenReturn("/api/rbac-verify");
        when(rbacSecurityProperties.getParamUrl()).thenReturn("url");
        when(rbacSecurityProperties.getParamMethod()).thenReturn("method");
        when(rbacSecurityProperties.getParamAppName()).thenReturn("appName");
        when(rbacSecurityProperties.getAppName()).thenReturn("appName");
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = createServer(restTemplate);
        roleGrantAttribute = new RoleGrantAttribute("test");
        passwdDateGrantAttribute = new PasswdDateGrantAttribute(1);
        server.expect(requestTo(rbacSecurityProperties.getUaaVerifierUri()+"?url=test&method=GET&appName=appName"))
                .andExpect(method(HttpMethod.GET)).andRespond(withSuccess(TestUtil.convertObjectToJsonBytes(
                        Arrays.asList(roleGrantAttribute.getAttribute(),passwdDateGrantAttribute.getAttribute())),
                        MediaType.APPLICATION_JSON));
        rbacRequestVerifierClient = new UaaRBACRequestVerifierClient(rbacSecurityProperties, restTemplate);
    }
    @Test
    public void getConfigAttribute() throws Exception {
        assertThat(rbacRequestVerifierClient.getConfigAttribute("test",HttpMethod.GET))
                .contains(roleGrantAttribute,passwdDateGrantAttribute);
    }
}
