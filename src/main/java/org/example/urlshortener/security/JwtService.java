package org.example.urlshortener.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000
                        )
                )
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}







// package org.example.urlshortener.security;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Service;

// import javax.crypto.SecretKey;
// import java.util.Date;

// @Service
// public class JwtService {

//         @Value("${jwt.secret}")
//         private String secret;

//         private SecretKey getKey() {
//             return Keys.hmacShaKeyFor(
//                     secret.getBytes()
//             );
//         }

// //     private static final String SECRET =
// //             "myVerySecretKeyForJwtGenerationMustBeLongEnough123456";

//     private final SecretKey key =
//             Keys.hmacShaKeyFor(
//                     SECRET.getBytes()
//             );

//     public String generateToken(
//             String username
//     ) {

//         return Jwts.builder()

//                 .subject(username)

//                 .issuedAt(
//                         new Date()
//                 )

//                 .expiration(
//                         new Date(
//                                 System.currentTimeMillis()
//                                         + 86400000
//                         )
//                 )

//                 .signWith(key)

//                 .compact();
//     }

//     public String extractUsername(
//             String token
//     ) {
    
//         return Jwts.parser()
//                 .verifyWith(key)
//                 .build()
//                 .parseSignedClaims(token)
//                 .getPayload()
//                 .getSubject();
//     }
// }