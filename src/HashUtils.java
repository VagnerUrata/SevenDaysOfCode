import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class HashUtils {

	public static String getHashMd5(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));
			return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}

record Serie(String title, String urlImage, String rating, String year) implements Content{

	@Override
	public int compareTo(Content c) {
		return this.rating().compareTo(c.rating());
	}
}
