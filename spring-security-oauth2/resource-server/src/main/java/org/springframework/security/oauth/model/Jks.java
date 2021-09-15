package org.springframework.security.oauth.model;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
public class Jks {
    private KeyStoreKeyFactory storeKeyFactory;
    private String verifierKeyId;
    private KeyPair keyPair;

    public String getVerifierKeyId() {
        return verifierKeyId;
    }

    public Jks(String keyAlias, String keyStoreFile, String keyStorePassword) {
        storeKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStoreFile), keyStorePassword.toCharArray());
        verifierKeyId = new String(Base64.encode(KeyGenerators.secureRandom(32).generateKey()));
        keyPair = storeKeyFactory.getKeyPair(keyAlias);
    }

    public RSAPublicKey getPublicKey() {
        return (RSAPublicKey) keyPair.getPublic();
    }

    public RSAPrivateKey getPrivateKey() {
        return (RSAPrivateKey) keyPair.getPrivate();
    }
}