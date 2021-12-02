package cn.com.xuxiaowei.resource.server.configuration;

import cn.com.xuxiaowei.resource.server.properties.KeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * 默认 Token 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class DefaultTokenConfiguration {

    private KeyProperties keyProperties;

    @Autowired
    public void setKeyProperties(KeyProperties keyProperties) {
        this.keyProperties = keyProperties;
    }

    /**
     * 加密 Token {@link Bean}
     * <p>
     * 在 {@link JwtAccessTokenConverter} 对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link JwtAccessTokenConverter} 对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        // 加密 Token
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        KeyProperties.KeyStore keyStore = keyProperties.getKeyStore();
        Resource location = keyStore.getLocation();
        String alias = keyStore.getAlias();
        String password = keyStore.getPassword();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(location, password.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, password.toCharArray());

        jwtAccessTokenConverter.setKeyPair(keyPair);

        return jwtAccessTokenConverter;
    }

    /**
     * 资源服务器令牌服务 服务接口 {@link Bean}
     * <p>
     * 在 {@link ResourceServerTokenServices} 实现类对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link ResourceServerTokenServices} 实现类对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     */
    @Bean
    @ConditionalOnMissingBean
    public ResourceServerTokenServices resourceServerTokenServices(JwtAccessTokenConverter jwtAccessTokenConverter) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        // Jwt Token 缓存
        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtAccessTokenConverter);

        // 设置 Jwt Token 缓存
        tokenServices.setTokenStore(jwtTokenStore);

        return tokenServices;
    }

}
