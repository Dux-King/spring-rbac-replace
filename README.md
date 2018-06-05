

# RBAC 权限控制说明

## 1. Server

### 1.1 配置

```java
@EnableRBACSecurityServer
@Configuration
public class UaaConfigAttributeExtractor implements ConfigAttributeExtractor {
      
   @Override
   public Collection<ConfigAttribute> obtainConfigAttributes(String url, HttpMethod httpMethod, String appName) {
//添加方法获取ConfigAttribute集合
   }

    @Override
    public Collection<ConfigAttribute> supplementConfigAttributes() {
      //添加添加的其他ConfigAttribute，比如有效期
    }
}
```

## 2. Client

### 2.1 配置

#### 2.1.1 使能配置

```java
@Configuration
@EnableRBACSecurityClient
public class RBACConfiguration extends RBACSecurityClientConfig {
    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    public RestTemplate restTemplate;
    @Override
    public RestTemplate loadBalancedRestTemplate() {
        return restTemplate;
    }
}
```



#### 2.1.2 添加RestTemplate

```
@Configuration
public class RestConfiguration {
    @Bean
    @Qualifier("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateCustomizer customizer) {
        RestTemplate restTemplate = new RestTemplate();
        customizer.customize(restTemplate);
        return restTemplate;
    }
}
```

#### 2.1.3 添加过滤

```java
    @Autowired
	private RBACSecurityInterceptor rbacSecurityInterceptor;    
......//省略
	public void configure(HttpSecurity http) throws Exception {
        http
        ......//省略
        .and()
            .authorizeRequests().anyRequest().permitAll()
        .and()
            .addFilterAfter(rbacSecurityInterceptor,FilterSecurityInterceptor.class);
```

#### 2.1.4 yml 配置

```yaml
rbacSecurity:
    ignore-url:
        - /path/abc/*
        - /path/def/**/*.html
    base-url: http://managementuaa
```

ignore-url: 忽略不需要校验的url

base-url: http://managementuaa 

uaa  base路径

### 2.2 注意

- RestTemplate bean 不能在注入RBACSecurityInterceptor的configuration配置类中注解@Bean

  ​如下：不能放在注入了RBACSecurityInterceptor的类中，否则会出现回环依赖错误

```java
    @Bean
    @Qualifier("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateCustomizer customizer) {
        RestTemplate restTemplate = new RestTemplate();
        customizer.customize(restTemplate);
        return restTemplate;
    }

    @Bean
    @Qualifier("vanillaRestTemplate")
    public RestTemplate vanillaRestTemplate() {
        return new RestTemplate();
    }
```

