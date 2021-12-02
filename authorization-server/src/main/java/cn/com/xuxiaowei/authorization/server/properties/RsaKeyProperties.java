package cn.com.xuxiaowei.authorization.server.properties;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA
 *
 * @author xuxiaowei
 * @see <a href="https://github.com/xuxiaowei-com-cn/RSA">RSA 非对称性加密、签名工具</a>
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties("rsa")
public class RsaKeyProperties {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 获取公钥
     *
     * @return 返回 公钥
     * @throws InvalidKeyException 秘钥不合法
     */
    public RSAPublicKey rsaPublicKey() throws InvalidKeyException {
        return RSAPublicKeyImpl.newKey(Base64.decodeBase64(publicKey));
    }

    /**
     * 获取私钥
     *
     * @return 返回 私钥
     * @throws InvalidKeyException 秘钥不合法
     */
    public RSAPrivateKey rsaPrivateKey() throws InvalidKeyException {
        return RSAPrivateCrtKeyImpl.newKey(Base64.decodeBase64(privateKey));
    }

}
