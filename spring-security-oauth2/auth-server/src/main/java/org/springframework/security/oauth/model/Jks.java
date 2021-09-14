/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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