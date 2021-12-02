package cn.com.xuxiaowei.resource.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * 程序执行入口
 *
 * @author xuxiaowei
 * @see ExceptionTranslationFilter 异常翻译过滤器
 * @see org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer 授权服务器安全配置器
 * @see org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator 默认 Web 响应异常转换器
 * @see JwtTokenStore#readAccessToken(String) 解密Token
 * @since 0.0.1
 */
@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}
