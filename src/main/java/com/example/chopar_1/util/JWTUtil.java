package com.example.chopar_1.util;




import com.example.chopar_1.dto.JwtDTO;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.ProfileRole;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWTUtil {
    private static final int tokenLiveTime=1000*3600*24;
    private static  final String secretKey="fhifjhffnrueialrewrrrjfsruytrettdft46uhgyihkhgkgfftrmhgfffrtrmyuk,illkjkj";


    public static String encode(String email, ProfileRole role, AppLanguage appLanguage) {

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("appLanguage", appLanguage);

        jwtBuilder.claim("email", email);
        jwtBuilder.claim("role", role);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        //  jwtBuilder.issuer("YouTube");
        return jwtBuilder.compact();


       /* JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);

        jwtBuilder.claim("profileId", profileId);
        jwtBuilder.claim("role", role);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.issuer("kunuz test portali");
        return jwtBuilder.compact();*/
    }
   //     try {


           /* //The JWT signature algorithm we will be using to sign the token
            String jwtToken = Jwts.builder().subject("role").issuedAt(role)
                    .setAudience("id")
                    .signWith(SignatureAlgorithm.HS256,secretKey.getBytes()).compact();

            System.out.println("jwtToken=");
            System.out.println(jwtToken);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());*/


    public static JwtDTO decode(String token) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        String id = (String) claims.get("email");
        String role = (String) claims.get("role");
        String appLanguage=(String) claims.get("appLanguage") ;
        AppLanguage appLanguage1=AppLanguage.valueOf(appLanguage);
        ProfileRole profileRole = ProfileRole.valueOf(role);

        return new JwtDTO(id, profileRole,appLanguage1);
    }

}
