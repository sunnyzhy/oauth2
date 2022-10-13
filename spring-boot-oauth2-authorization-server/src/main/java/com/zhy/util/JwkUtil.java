package com.zhy.util;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.*;
import org.springframework.security.oauth2.jose.JwaAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.util.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * @author zhy
 * @date 2022/10/12 11:28
 * <p>
 * JWT 采用的是非对称加密，所以其加密算法不包含 AES
 * <p>
 * 参考:
 * https://www.rfc-editor.org/rfc/rfc7518
 * https://connect2id.com/products/nimbus-jose-jwt/examples/jwk-generation#:~:text=The%20octet%20sequence%20JWK%20format%20is%20intended%20for,must%20match%20the%20size%20of%20the%20output%20hash.
 */
public class JwkUtil {
    public static JWK rsa(RSA rsa) throws NoSuchAlgorithmException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance(rsa.name);
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .algorithm(rsa.alg)
                .build();
        return rsaKey;
    }

    public static JWK ec(EC ec) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ec.name);
        gen.initialize(ec.curve.toECParameterSpec());
        KeyPair keyPair = gen.generateKeyPair();

        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        ECKey ecKey = new ECKey.Builder(ec.curve, publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .algorithm(ec.alg)
                .build();
        return ecKey;
    }

    /**
     * 随机生成秘钥
     *
     * @param hmac
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static JWK hmac(HMAC hmac) throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance(hmac.name).generateKey();

        OctetSequenceKey octetSequenceKey = new OctetSequenceKey.Builder(secretKey)
                .keyID(UUID.randomUUID().toString())
                .algorithm(hmac.alg)
                .build();
        return octetSequenceKey;
    }

    /**
     * 指定秘钥
     *
     * @param secret 秘钥
     * @param hmac
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static JWK hmac(String secret, HMAC hmac) {
        Assert.hasLength(secret, "secret must not be null");
        byte[] bytes = secret.getBytes();
        byte[] var1 = new byte[hmac.size + 7 >> 3];
        for (int i = 0; i < bytes.length; i++) {
            var1[i] = bytes[i];
        }
        SecretKey secretKey = new SecretKeySpec(var1, hmac.name);

        OctetSequenceKey octetSequenceKey = new OctetSequenceKey.Builder(secretKey)
                .keyID(UUID.randomUUID().toString())
                .algorithm(hmac.alg)
                .build();
        return octetSequenceKey;
    }

    public enum RSA {
        RS256("RSA", 256, JWSAlgorithm.RS256, SignatureAlgorithm.RS256),
        RS384("RSA", 384, JWSAlgorithm.RS384, SignatureAlgorithm.RS384),
        RS512("RSA", 512, JWSAlgorithm.RS512, SignatureAlgorithm.RS512);

        private final String name;
        private final Integer size;
        private final Algorithm alg;
        private final JwaAlgorithm jwaAlg;

        RSA(String name, Integer size, Algorithm alg, JwaAlgorithm jwaAlg) {
            this.name = name;
            this.size = size;
            this.alg = alg;
            this.jwaAlg = jwaAlg;
        }
    }

    public enum EC {
        ES256("EC", 256, Curve.P_256, JWSAlgorithm.ES256, SignatureAlgorithm.ES256),
        ES384("EC", 384, Curve.P_384, JWSAlgorithm.ES384, SignatureAlgorithm.ES384),
        ES512("EC", 512, Curve.P_521, JWSAlgorithm.ES512, SignatureAlgorithm.ES512);

        private final String name;
        private final Integer size;
        private final Curve curve;
        private final Algorithm alg;
        private final JwaAlgorithm jwaAlg;

        EC(String name, Integer size, Curve curve, Algorithm alg, JwaAlgorithm jwaAlg) {
            this.name = name;
            this.size = size;
            this.curve = curve;
            this.alg = alg;
            this.jwaAlg = jwaAlg;
        }
    }

    public enum HMAC {
        HS256("HmacSHA256", 256, JWSAlgorithm.HS256, MacAlgorithm.HS256),
        HS384("HmacSHA384", 384, JWSAlgorithm.HS384, MacAlgorithm.HS384),
        HS512("HmacSHA512", 512, JWSAlgorithm.HS512, MacAlgorithm.HS512);

        private final String name;
        private final Integer size;
        private final Algorithm alg;
        private final JwaAlgorithm jwaAlg;

        HMAC(String name, Integer size, Algorithm alg, JwaAlgorithm jwaAlg) {
            this.name = name;
            this.size = size;
            this.alg = alg;
            this.jwaAlg = jwaAlg;
        }
    }
}
