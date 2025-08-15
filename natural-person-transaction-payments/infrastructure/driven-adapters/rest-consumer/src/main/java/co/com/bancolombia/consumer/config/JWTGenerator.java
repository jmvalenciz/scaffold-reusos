package co.com.bancolombia.consumer.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTGenerator {
    RSAPrivateKey privateKey;
    private final String issuer;
    private final String kid;
    private final int exp;


    public JWTGenerator(
            @Value("${jwt-config.private-key-path}") Resource secretKeyFile,
            @Value("${jwt-config.issuer}") String issuer,
            @Value("${jwt-config.kid}") String kid,
            @Value("${jwt-config.expiration-seconds}") int exp) throws GeneralSecurityException, IOException {
        this.issuer = issuer;
        this.kid = kid;
        this.exp = exp;
        this.privateKey = loadPrivateKeyFromPem(secretKeyFile);
    }

    public String createJwtForSubject(String subject) {
        try {
            JWSSigner signer = new RSASSASigner(privateKey);

            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(issuer)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(exp)))
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(kid)
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sign JWT", e);
        }
    }

    private static RSAPrivateKey loadPrivateKeyFromPem(Resource file) throws GeneralSecurityException, IOException {
        String pem = file.getContentAsString(Charset.defaultCharset());
        String base64 = pem
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(base64);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }
}
