package cn.com.xuxiaowei.authorization.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerTokenServicesConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.*;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;

/**
 * ???????????? ??????
 *
 * @author xuxiaowei
 * @see DefaultLoginPageGeneratingFilter ??????????????????
 * @see OAuth2ClientAuthenticationProcessingFilter ?????? OAuth2 ?????????????????????????????????????????????????????? OAuth2 ???????????????????????????????????????????????? SecurityContext
 * @see AuthorizationServerEndpointsConfiguration#authorizationEndpoint() ???????????????
 * @see AuthorizationServerSecurityConfiguration Token ????????????????????????
 * @see ClientCredentialsTokenEndpointFilter OAuth2 ????????????????????????????????????????????????
 * @see WhitelabelApprovalEndpoint ?????????????????????????????????????????????????????????
 * @see WhitelabelErrorEndpoint ??????????????????????????????????????????????????????
 * @see AuthorizationEndpoint#authorize(Map, Map, SessionStatus, Principal) ?????????
 * @see AuthorizationEndpoint#approveOrDeny(Map, Map, SessionStatus, Principal) ?????????
 * @see TokenEndpoint#getAccessToken(Principal, Map) ?????? Token
 * @see TokenEndpoint#postAccessToken(Principal, Map) ?????? Token
 * @see TokenKeyEndpoint#getKey(Principal) ?????? ???????????????????????????
 * @see CheckTokenEndpoint#checkToken(String) ?????? Token
 * @see WhitelabelErrorEndpoint#handleError(HttpServletRequest) ??????
 * @see JwtAccessTokenConverter#enhance(OAuth2AccessToken, OAuth2Authentication) ??????Token
 * @see JwtTokenStore#readAccessToken(String) ??????Token
 * @see AuthorizationServerTokenServicesConfiguration
 * @see <a href="http://127.0.0.1:10201/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">?????? code</a>
 * @see <a href="http://127.0.0.1:10201/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=code&scope=snsapi_userinfo&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">?????? code</a>
 * @see <a href="http://127.0.0.1:10201/oauth/authorize?client_id=5e03fb292edd4e478cd7b4d6fc21518c&redirect_uri=http://127.0.0.1:123&response_type=token&scope=snsapi_base&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7">?????? Token???implicit??????????????????</a>
 * @see <a href="http://127.0.0.1:10201/oauth/token?code=oW9ca3NuaVHeWRwUJoMscLuI91ahS8rw&client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&redirect_uri=http://127.0.0.1:123&grant_type=authorization_code">?????? Token</a>
 * @see <a href="http://127.0.0.1:10201/oauth/token?client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&grant_type=refresh_token&refresh_token=">?????? Token</a>
 * @see <a href="http://127.0.0.1:10201/oauth/token?grant_type=client_credentials&client_id=5e03fb292edd4e478cd7b4d6fc21518c&client_secret=da4ce585e30346d3a876340d49e25a01&scope=snsapi_base snsapi_userinfo">????????? client_credentials</a>
 * @see <a href="http://127.0.0.1:10201/oauth/check_token?token=">?????? Token??????????????? POST???</a>
 * @since 0.0.1
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurerAdapterConfiguration extends AuthorizationServerConfigurerAdapter {

    private DataSource dataSource;

    private TokenStore tokenStore;

    private TokenEnhancer tokenEnhancer;

    private AuthorizationCodeServices authorizationCodeServices;

    private UserDetailsService userDetailsService;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Autowired
    public void setTokenEnhancer(TokenEnhancer tokenEnhancer) {
        this.tokenEnhancer = tokenEnhancer;
    }

    @Autowired
    public void setAuthorizationCodeServices(AuthorizationCodeServices authorizationCodeServices) {
        this.authorizationCodeServices = authorizationCodeServices;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        // ?????? Token???????????????
        security.tokenKeyAccess("permitAll()");
        // ?????? Token???????????????
        security.checkTokenAccess("permitAll()");
        // ?????? Client ?????????????????????URL????????????????????????????????? ClientId???ClientSecret
        security.allowFormAuthenticationForClients();

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // ???????????????
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // ?????? Token ????????? GET???POST
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        // ?????? Token ???????????????
        endpoints.userDetailsService(userDetailsService);

        // code ?????????
        endpoints.authorizationCodeServices(authorizationCodeServices);

        // Token ????????????
        endpoints.tokenStore(tokenStore);

        // Token ??????
        endpoints.tokenEnhancer(tokenEnhancer);

    }

}
