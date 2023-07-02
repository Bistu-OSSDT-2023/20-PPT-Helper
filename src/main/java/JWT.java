import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Date;
// 这玩意写了半天用不了，谁有空可以改改改
public class JWT {
    public static void main(String[] args) {
        // 分割 API Key 得到 ID 和 Secret
        String apiKey = "e19168e73ac3a499d2b4bace308a7147.VjWBSaOi8jlr0YLn"; //将此值替换为你的API Key
        String[] parts = apiKey.split("\\.");
        String id = parts[0];
        String secret = parts[1];

        // 使用 SHA-256 哈希函数对 secret 进行哈希，生成一个256位的密钥
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            throw new RuntimeException("Unable to get SHA-256 Message Digest", e);
        }
        byte[] hashedSecret = digest.digest(secret.getBytes());

        SecretKeySpec secretKeySpec = new SecretKeySpec(hashedSecret, "HmacSHA256");

        // 创建JWT
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // 设置一个小时后过期
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        String jws = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("sign_type", "SIGN")
                .claim("api_key", id)
                .claim("exp", expMillis)
//                .claim("exp", 1682503829130L)
                .claim("timestamp", nowMillis)
//                .claim("timestamp", 1682503820130L)
                .signWith(secretKeySpec, SignatureAlgorithm.HS256)
                .compact();

        // 打印生成的 JWT
        System.out.println(jws);
    }
}
