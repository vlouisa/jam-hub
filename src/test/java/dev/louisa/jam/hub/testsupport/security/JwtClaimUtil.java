package dev.louisa.jam.hub.testsupport.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;

import java.util.List;
import java.util.Map;

public class JwtClaimUtil {
    public static List<String> getClaims(String rawJwt) {
        return JWT.decode(rawJwt).getClaims().entrySet().stream()
                .map(JwtClaimUtil::getClaimValue)
                .toList();
    }

    private static String getClaimValue(Map.Entry<String, Claim> e) {
        final String claim = e.getKey() + ":" + e.getValue();
        return claim.replace("\"", "");
    }
}