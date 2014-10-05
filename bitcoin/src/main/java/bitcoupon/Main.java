package bitcoupon;

public class Main {

  private static final String GENERATE_CREATION_TRANSACTION = "generateCreationTransaction";
  private static final String GENERATE_SEND_TRANSACTION = "generateSendTransaction";
  private static final String GET_CREATOR_ADDRESSES = "getCreatorAddresses";
  private static final String VERIFY_TRANSACTION = "verifyTransaction";

  public static void main(String[] args) {
    evaluateMethod(args);

//    testTransaction();
  }

  private static void evaluateMethod(String[] args) {
    if (args.length == 0) {
      System.out.println("Available methods:");
      System.out.println("Name: " + GENERATE_CREATION_TRANSACTION + " - Argumentss: String privateKey");
      System.out.println("Name: " + GENERATE_SEND_TRANSACTION
                         + " - Argumentss: String privateKey, String creatorAddress, String transactionHistoryJson, String receiverAddress");
      System.out.println(
          "Name: " + GET_CREATOR_ADDRESSES + " - Argumentss: String privateKey, String transactionHistoryJson");
      System.out.println(
          "Name: " + VERIFY_TRANSACTION + " - Argumentss: String transactionJson, String transactionHistoryJson");
    } else {

      String methodName = args[0];

      checkArgumentsLength(methodName, args.length);

      if (methodName.equalsIgnoreCase(GENERATE_CREATION_TRANSACTION)) {
        BitCouponCli.generateCreationTransaction(args[1]);
      } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
        BitCouponCli.generateSendTransaction(args[1], args[2], args[3], args[4]);
      } else if (methodName.equalsIgnoreCase(GET_CREATOR_ADDRESSES)) {
        BitCouponCli.getCreatorAddresses(args[1], args[2]);
      } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
        BitCouponCli.verifyTransaction(args[1], args[2]);
      }
    }
  }

  private static void checkArgumentsLength(String methodName, int argsLength) {

    boolean correct = checkLength(methodName, argsLength);
    if (!correct) {
      String message = "Wrong number of arguments to method: " + methodName;

      // TODO Choose between exception or exit with failure.
      //throw new IllegalArgumentException("Wrong number of arguments to method: " + methodName);

      System.err.println("Wrong number of arguments to method: " + methodName);
      System.exit(1);
    }
  }

  private static boolean checkLength(String methodName, int argsLength) {
    int length = 0;
    if (methodName.equalsIgnoreCase(GENERATE_CREATION_TRANSACTION)) {
      length = 2;
    } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
      length = 5;
    } else if (methodName.equalsIgnoreCase(GET_CREATOR_ADDRESSES)) {
      length = 3;
    } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
      length = 3;
    }
    return length == argsLength;
  }
}
