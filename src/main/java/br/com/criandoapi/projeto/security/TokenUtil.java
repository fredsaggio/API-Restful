package br.com.criandoapi.projeto.security;

import br.com.criandoapi.projeto.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

public class TokenUtil {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION = 1000*60*60*12;
    private static final String SECRET_KEY = "fredericosaggioestudandoapilegal";
    private static final String EMISSOR = "SouLegal";


    // Metodo que cria o token
    public static String createToken(Usuario usuario) {

        Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        String token = Jwts.builder()
                .setSubject(usuario.getNome())
                .setIssuer(EMISSOR)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return PREFIX + token;
    }

    // Metodos que fazem as validações do token
     private static boolean isExpirationValid(Date expiration) {
        return expiration.after(new Date(System.currentTimeMillis()));
     }

     private static boolean isEmissorValid(String emissor) {
        return emissor.equals(EMISSOR);
     }

     private static boolean isSubjectValid(String username) {
        return username != null && !username.isEmpty();
     }

     public static Authentication validate(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
         if (token != null && token.startsWith(PREFIX)) {
             token = token.replace(PREFIX, "");
         } else {
             return null; // Token inválido
         }

         Jws<Claims> jwsClaims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes())
                 .build()
                 .parseClaimsJws(token);

         String username = jwsClaims.getBody().getSubject();
         String issuer = jwsClaims.getBody().getIssuer();
         Date expiration = jwsClaims.getBody().getExpiration();

         if(isSubjectValid(username) && isExpirationValid(expiration) && isEmissorValid(issuer)) {
             return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()); // O null pode ser o que o usuário é, Admin, usuário comum e etc.

             // No collections são os endpoints que o usuário pode acessar.
         }
        return null;
     }
}
