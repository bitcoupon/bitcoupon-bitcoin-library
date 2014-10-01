package bitcoupon;

public class Main {

  private static final String GENERATE_CREATION_TRANSACTION = "generateCreationTransaction";
  private static final String GENERATE_SEND_TRANSACTION = "generateSendTransaction";
  private static final String GET_CREATOR_PUBLIC_KEYS = "getCreatorPublicKeys";
  private static final String VERIFY_TRANSACTION = "verifyTransaction";

  public static void main(String[] args) {
    evaluateMethod(args);

//    testTransaction();
  }



  private static void evaluateMethod(String[] args) {
    String methodName = args[0];

    if (methodName.equalsIgnoreCase(GENERATE_CREATION_TRANSACTION)) {
      BitCouponCli.generateCreationTransaction(args[1]);
    } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
      BitCouponCli.generateSendTransaction(args[1], args[2], args[3], args[4]);
    } else if (methodName.equalsIgnoreCase(GET_CREATOR_PUBLIC_KEYS)) {
      BitCouponCli.getCreatorPublicKeys(args[1]);
    } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
      BitCouponCli.verifyTransaction(args[1], args[2]);
    }
  }

}
