package org.springframework.security.oauth.config.authorization.server;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth.model.Jks;
import org.springframework.security.oauth.model.UserDetailsImpl;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class JwtConfig {
    private Jks jks = new Jks("oauth2-demo-key", ".keystore-oauth2-demo", "admin1234");

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final RsaSigner signer = new RsaSigner(jks.getPrivateKey());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            private JsonParser objectMapper = JsonParserFactory.create();

            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                final Map<String, Object> additionalInformation = new HashMap<>();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getUserAuthentication().getPrincipal();
                String tokenId = UUID.randomUUID().toString().replaceAll("-", "");
                additionalInformation.put("sub", userDetails.getUsername());
                additionalInformation.put("id", tokenId);
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                return super.enhance(accessToken, authentication);
            }

            @Override
            protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String content;
                try {
                    Map<String, Object> payload = (Map<String, Object>) getAccessTokenConverter().convertAccessToken(accessToken, authentication);
                    content = this.objectMapper.formatMap(payload);
                } catch (Exception ex) {
                    throw new IllegalStateException("Cannot convert access token to JSON", ex);
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("kid", jks.getVerifierKeyId());
                String token = JwtHelper.encode(content, signer, headers).getEncoded();
                return token;
            }
        };
        converter.setSigner(signer);
        converter.setVerifier(new RsaVerifier(jks.getPublicKey()));
        return converter;
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder(jks.getPublicKey())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(jks.getVerifierKeyId());
        return new JWKSet(builder.build());
    }
}
