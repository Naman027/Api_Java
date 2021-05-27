package net.proj.springboot.utility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.proj.springboot.loginEmployee.EmployeeEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.proj.springboot.model.Employee;
import net.proj.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import net.proj.springboot.exception.GenericException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public String generateToken(EmployeeEntry ep) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, ep.getUserName());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*60*1000))
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

    public void savingEmployee(@RequestBody EmployeeEntry ep){
        Employee newEmployee = new Employee();
        newEmployee.setEmail(ep.getEmailId());
        newEmployee.setPassword(ep.getPassword());
        newEmployee.setName(ep.getUserName());
        employeeRepository.save(newEmployee);
    }

}
