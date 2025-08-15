package com.example.customer.util;

import com.example.customer.dto.JwtDTO;
import com.example.customer.enums.AppLanguage;
import com.example.customer.enums.ProfileRole;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWTUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24;

    private static final String secretKey = "fhifjhffnrueialrewrrrjfsruytrettdft46uhgyihkhgkgfftrmhgfffrtrmyukillkjkj";

    public static String encode(String phone, String email, ProfileRole role, AppLanguage appLanguage) {

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        return Jwts.builder()
                .setIssuedAt(new Date())
                .signWith(secretKeySpec, sa)
                .claim("phone", phone)
                .claim("email", email)
                .claim("role", role)
                .claim("appLanguage", appLanguage)
                .setExpiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .compact();
    }


    // JWT tokenni dekod qilish
    public static JwtDTO decode(String token) {

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        // JWT parser yaratish
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKeySpec)
                .build();

        // Tokenni parsilash va claims'larni olish
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);
        Claims claims = jws.getBody();

        String phone = claims.get("phone", String.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        String appLanguage = claims.get("appLanguage", String.class);

        // Enum tiplarini olish
        AppLanguage appLanguageEnum = AppLanguage.valueOf(appLanguage);
        ProfileRole profileRoleEnum = ProfileRole.valueOf(role);

        if (email != null) {
            return  JwtDTO.builder()
                    .email(email)
                    .appLanguage(appLanguageEnum)
                    .role(profileRoleEnum)
                    .build();
        }
        return JwtDTO.builder()
                .phone(phone)
                .role(profileRoleEnum)
                .appLanguage(appLanguageEnum)
                .build();
    }
}


