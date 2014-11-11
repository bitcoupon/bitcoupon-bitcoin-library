package bitcoupon.transaction;

import com.google.gson.Gson;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.DERSequenceGenerator;
import org.spongycastle.asn1.DLSequence;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import bitcoupon.Bitcoin;

public class OutputHistoryRequest {

  private final String address;
  private String signature;

  public OutputHistoryRequest(String address) {
    this.address = address;
    this.signature = "";
  }

  public String getAddress() {
    return address;
  }

  public String getSignature() {
    return signature;
  }

  public boolean signOutputHistoryRequest(BigInteger privateKey) {
    ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(privateKey, Bitcoin.EC_PARAMS);
    ECDSASigner signer = new ECDSASigner();
    signer.init(true, privateKeyParams);
    byte[] bAddress;
    try {
      bAddress = address.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    }
    BigInteger[] rawSignature = signer.generateSignature(bAddress);
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(72);
      DERSequenceGenerator derGen = new DERSequenceGenerator(baos);
      derGen.addObject(new ASN1Integer(rawSignature[0]));
      derGen.addObject(new ASN1Integer(rawSignature[1]));
      derGen.close();
      byte[] ecdsaSignature = baos.toByteArray();
      byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
      signature = Bitcoin.encodeBase58(ecdsaSignature) + " " + Bitcoin.encodeBase58(publicKey);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean verifySignature() {
    byte[] bAddress;
    try {
      bAddress = address.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    }
    byte[] ecdsaSignature;
    byte[] publicKey;
    try {
      ecdsaSignature = Bitcoin.decodeBase58(signature.split(" ")[0]);
      publicKey = Bitcoin.decodeBase58(signature.split(" ")[1]);
    } catch (IllegalArgumentException e) {
      return false;
    }
    if (!Bitcoin.publicKeyToAddress(publicKey).equals(address)) {
      return false;
    }
    ECPublicKeyParameters
        publicKeyParams =
        new ECPublicKeyParameters(Bitcoin.EC_PARAMS.getCurve().decodePoint(publicKey), Bitcoin.EC_PARAMS);
    ECDSASigner signer = new ECDSASigner();
    signer.init(false, publicKeyParams);
    try {
      ASN1InputStream derSigStream = new ASN1InputStream(ecdsaSignature);
      DLSequence seq = (DLSequence) derSigStream.readObject();
      BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
      BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
      derSigStream.close();
      if (!signer.verifySignature(bAddress, r, s)) {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static OutputHistoryRequest fromJson(String outputHistoryRequestJson) {
    return new Gson().fromJson(outputHistoryRequestJson, OutputHistoryRequest.class);
  }

  public static String toJson(OutputHistoryRequest outputHistoryRequest) {
    return new Gson().toJson(outputHistoryRequest, OutputHistoryRequest.class);
  }

}
