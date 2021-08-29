package org.example.infrastructure;

import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Default
@Singleton
public class JWTService {

    @Inject
    private Logger logger;

    public String generateJWT() {
        String key = readPemFile();
        JWTAuth provider = JWTAuth.create(null, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setBuffer(key)
                ));

        MPJWTToken token = new MPJWTToken();
        token.setAud("targetService");
        token.setIss("https://server.example.com");  // Must match the expected issues configuration values
        token.setJti(UUID.randomUUID().toString());
        token.setSub("Jessie");  // Sub is required for WildFly Swarm
        token.setUpn("Jessie");
        token.setIat(System.currentTimeMillis());
        token.setExp(System.currentTimeMillis() + 30000); // 30 Seconds expiration!
        token.addAdditionalClaims("custom-value", "Jessie specific value");
        token.setGroups(Arrays.asList("user", "protected"));

        return provider.generateToken(
                new io.vertx.core.json.JsonObject().mergeIn(token.toJSONString()),
                new JWTOptions().setAlgorithm("RS256"));
    }

    // NOTE:   Expected format is PKCS#8 (BEGIN PRIVATE KEY) NOT PKCS#1 (BEGIN RSA PRIVATE KEY)
    // See gencerts.sh
    private String readPemFile() {
        String key = null;
        try {
            key = readResourceAsString("privateKey.pem");
            key = stripX509PEMHeadersKey(key);
        } catch (IOException | URISyntaxException e) {
            logger.error("Error reading private key file", e);
        }
        return key;
    }

    private String readResourceAsString(String resourceName) throws URISyntaxException, IOException {
        List<String> fileLines = Files.readAllLines(
                Path.of(JWTService.class.getClassLoader().getResource(resourceName).toURI()),
                StandardCharsets.US_ASCII);
        return fileLines.stream().collect(Collectors.joining()).trim();
    }

    private String stripX509PEMHeadersKey(String key) {
        // Strip various headers!
        return key.replaceAll("^-+BEGIN (RSA )?(PRIVATE|PUBLIC) KEY-+|-+END (RSA )?(PRIVATE|PUBLIC) KEY-+$", "");
    }
}
