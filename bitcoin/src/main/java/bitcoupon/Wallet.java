package bitcoupon;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Set;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Wallet {
	
	public static void main(String[] args) {
		new Wallet("0450863AD64A87AE8A2FE83C1AF1A8403CB53F53E486D8511DAD8A04887E5B23522CD470243453A299FA9E77237716103ABC11A1DF38855ED6F2EE187E9C582BA6", "");
	}
	
	private String publicKey;
	
	Wallet(String publicKey, String privateKey) {
		this.publicKey = publicKey;
		createCoupons("abc", 1);
	}
	
	Transaction createCoupons(String subType, int amount) {
		
		
		
		Creation creation = new Creation();
		creation.scriptPubKey = "OP_DUP OP_HASH160 " + "" + " OP_EQUALVERIFY OP_CHECKSIG";
		creation.subType = subType;
		creation.amount = amount;
		Output output = new Output();
		output.couponType = creation.scriptPubKey + " - " + creation.subType;
		output.amount = creation.amount;
		output.scriptPubKey = "OP_DUP OP_HASH160 " + "" + " OP_EQUALVERIFY OP_CHECKSIG";
		
		
		
		
		
		
		return null;
	}
	
	Set<Coupon> getCoupons() {
		return null;
	}
	
	private Set<Transaction> getTransactions(String publicKey) {
		return null;
	}
	
}
