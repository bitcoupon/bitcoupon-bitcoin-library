//package bitcoupon;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import bitcoupon.BitCoupon;
//import bitcoupon.transaction.Transaction;
//import bitcoupon.transaction.TransactionHistory;
//
///**
// * Created by Patrick on 01.10.2014.
// */
//public class BitcoinTest {
//
//  @Test
//  public void test_TransactionJsonConvertion() {
//    Transaction trans = BitCoupon.generateCreationTransaction("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT");
//    String json = Transaction.toJson(trans);
//    Transaction fromJson = Transaction.fromJson(json);
//    System.out.println(json);
//
//    Transaction trans2 = BitCoupon.generateCreationTransaction("5K4gQUNnxuJe1gtbCp4qrGysRXVdGE9jZW1vJZ1jdFzV6W93QDP");
//    Transaction trans3 = BitCoupon.generateCreationTransaction("5JcK7bvAFjCwTcERJsbRetGkwmqA8BuydVFRffrQj3LjuqWsTy5");
//    Transaction trans4 = BitCoupon.generateCreationTransaction("5JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA");
//
//    List<Transaction> transactionList = new ArrayList<>();
//    transactionList.add(trans2);
//    transactionList.add(trans3);
//    transactionList.add(trans4);
//
//    String jsonTransactionList = TransactionHistory.toJson(transactionList);
//    System.out.println(jsonTransactionList);
//    TransactionHistory listFromJson = TransactionHistory.fromJson(jsonTransactionList);
//  }
//
//  @Test
//  public void test_TransactionCreation() {
//    Map<String, Boolean> privateKeys = new HashMap<>();
//    privateKeys.put("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT", true);
//    privateKeys.put("5K4gQUNnxuJe1gtbCp4qrGysRXVdGE9jZW1vJZ1jdFzV6W93QDP", true);
//    privateKeys.put("5JcK7bvAFjCwTcERJsbRetGkwmqA8BuydVFRffrQj3LjuqWsTy5", true);
//    privateKeys.put("5JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA", true);
//    privateKeys.put("9JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA", false);
//    privateKeys.put("JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA", false);
//    privateKeys.put(
//        "JcasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA",
//        false);
//    privateKeys.put("sadasdasdasd", false);
//    privateKeys.put("0", false);
//    privateKeys.put("1", false);
//    privateKeys.put(null, false);
//    privateKeys.put(String.valueOf(new Integer(1).hashCode()), false);
//    for (String key : privateKeys.keySet()) {
//      Boolean value = privateKeys.get(key);
//      boolean valid = false;
//      try {
//        Transaction validTransaction = BitCoupon.generateCreationTransaction(key);
//        valid = true;
//      } catch (IllegalArgumentException e) {
//        valid = false;
//      }
//      Assert.assertEquals(valid, value);
//      System.out.println("" + valid + value);
//    }
//  }
//
//  @Test
//  public void test_PrivateKeyToAddress() {
//    Map<String, String> correctPrivateKeyAddresses = new HashMap<String, String>();
//    correctPrivateKeyAddresses.put("5JYKkQjmtyeKJV6i28gdCLKTVm74FWpLR61PktA6iEi8shMwJtc", "1KvbQ2umogKHXY8B5QMTk1D2q56iKuJjei");
//    correctPrivateKeyAddresses.put("5JUcaLHjfc37d2M1QdaB1KtTBdhxUawq6cyDtYf19Tr5t182kZs", "1qr45DDj4HprbDr96TnjjmpJwqQMHDgpz");
//    correctPrivateKeyAddresses.put("5JJPndnt6kYCpP8Ypq11Nhhsg7mSWmLDPSKnxC6zXXQxL4gPjiN", "1DNubDX2qWmuxPazKLnLLpkCTm4Av5fYMR");
//    correctPrivateKeyAddresses.put("5JX9kpYnRgYjiaFmuLqeHFrKG6UZUfJAMmHPzRvvvcWhUxjPeuD", "1FGcfhfvoQQU4cYfpxuFRr7S3bmVujt9mW");
//    correctPrivateKeyAddresses.put("5KJCevAHnxtEhz1N11QFWP8apmiCz8g6Ev511pp4JvE9JRtEfk1", "1H8eJGdocfyegUpQYmXjfLteL7ePvQ8raZ");
//    Map<String, String> incorrectPrivateKeyAddresses = new HashMap<String, String>();
//    incorrectPrivateKeyAddresses.put("5JYKkQjmtyeKJV6i28gdCLKTVm74FWpLR61PktA6iEi8shMwJtc", "KvbQ2umogKHXY8B5QMTk1D2q56iKuJjei");
//    incorrectPrivateKeyAddresses.put("5JUcaLHjfc37d2M1QdaB1KtTBdhxUawq6cyDtYf19Tr5t182kZs", "1qr45DDj4HprbDr96TjjmpJwqQMHDgpz");
//    for (String strPrivateKey : correctPrivateKeyAddresses.keySet()) {
//      BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
//      byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
//      String address = Bitcoin.publicKeyToAddress(publicKey);
//      Assert.assertEquals(address, correctPrivateKeyAddresses.get(strPrivateKey));
//    }
//    for (String strPrivateKey : incorrectPrivateKeyAddresses.keySet()) {
//      BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
//      byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
//      String address = Bitcoin.publicKeyToAddress(publicKey);
//      Assert.assertNotEquals(address, incorrectPrivateKeyAddresses.get(strPrivateKey));
//    }
//  }
//}
