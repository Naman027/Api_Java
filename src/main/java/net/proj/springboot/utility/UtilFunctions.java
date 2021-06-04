package net.proj.springboot.utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.proj.springboot.model.ReturnEmployeeWithToken;
import net.proj.springboot.model.StatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import net.proj.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import net.proj.springboot.exception.GenericException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UtilFunctions {

    enum Algorithms{
        HASH1("SHA-256"), HASH2("MD-5");
        String retString;
        Algorithms(String retString){
            this.retString  = retString;
        }
        Algorithms(){
            this.retString = "No Algo Taken";
        }
        public String getString(){
            return this.retString;
        }
    }

    @Autowired EmployeeRepository employeeRepository;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String emailId) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, emailId);
    }
    
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts.builder().setClaims(claims).setSubject(subject).claim("authorities",
                grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1*30*1000))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
        return "Bearer "+ token;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getEmailIdFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        final Date curDate = new Date(System.currentTimeMillis());
        return expiration.after(curDate);
    }
    
    public ResponseEntity<?> isLoggedIn(String token){
        try {
            boolean isStillLoggedIn = isTokenExpired(token);
            String emailIdOfToken = getEmailIdFromToken(token);
            ReturnEmployeeWithToken returnEmployeeWithToken = new ReturnEmployeeWithToken();
            returnEmployeeWithToken.setStatus("The User is currently logged in");
            String newToken = generateRefreshToken(emailIdOfToken);
            returnEmployeeWithToken.setStatusCode(1008);
            returnEmployeeWithToken.setToken(newToken);
            return new ResponseEntity<ReturnEmployeeWithToken>(returnEmployeeWithToken, HttpStatus.OK);
        }catch (ExpiredJwtException e){
            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setStatusCode(10010);
            statusResponse.setStatus("Token expired! Login Again");
            return new ResponseEntity<StatusResponse>(statusResponse,HttpStatus.NOT_FOUND);
        }
    }

    public String generateRefreshToken(String emailId) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshToken(claims, emailId);
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10*60*60*1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String hashedPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance(Algorithms.HASH1.getString());
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        }catch (NoSuchAlgorithmException e){
            throw new GenericException("No Algorithm Exception Occurred");
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
