package bitcoupon;

/**
 * This class provides a console interface for the class BitCouponCli.java.
 */
public class Main {

  private static final String GENERATE_CREATE_TRANSACTION = "generateCreateTransaction";
  private static final String GENERATE_SEND_TRANSACTION = "generateSendTransaction";
  private static final String VERIFY_TRANSACTION = "verifyTransaction";
  private static final String GET_COUPONS = "getCoupons";
  private static final String GET_COUPON_OWNERS = "getCouponOwners";
  private static final String GENERATE_PRIVATE_KEY = "generatePrivateKey";

  /**
   * This method takes a String array. It is passed on to evaluateMethod(args).
   */
  public static void main(String[] args) {
    evaluateMethod(args);

//    testTransaction();
  }

  /**
   * This method takes a String array. args[0] contains the method the programs that calls the library wishes to use.
   * These methods include:
   *
   * GENERATE_CREATION_TRANSACTION
   *
   * GENERATE_SEND_TRANSACTION
   *
   * GET_CREATOR_ADDRESSES
   *
   * VERIFY_TRANSACTION <p/>
   *
   * The rest of the tuples contains the argument for the corresponding method. This method reads which method is being
   * called, runs isValidArgumentsLength(), and if that checks out, sends the arguments to that method. If args[] is
   * empty, the method returns a list of available methods.
   */
  private static void evaluateMethod(String[] args) {
    if (args.length == 0) {
      System.out.println("Available methods:");
      System.out.println("Name: " + GENERATE_CREATE_TRANSACTION + " - Arguments: String strPrivateKey, String payload");
      System.out.println("Name: " + GENERATE_SEND_TRANSACTION + " - Arguments: String strPrivateKey, String couponJson, String receiverAddress, String outputHistoryJson");
      System.out.println("Name: " + VERIFY_TRANSACTION + " - Arguments: String transactionJson, String outputHistoryJson");
      System.out.println("Name: " + GET_COUPONS + " - Arguments: String strPrivateKey, String outputHistoryJson");
      System.out.println("Name: " + GET_COUPON_OWNERS + " - Arguments: String creatorAddress, String payload, String outputHistoryJson");
      System.out.println("Name: " + GENERATE_PRIVATE_KEY + " - Arguments: none");
    } else {

      String methodName = args[0];

      if (!isValidArgumentsLength(methodName, args.length)) {
        return;
      }

      if (methodName.equalsIgnoreCase(GENERATE_CREATE_TRANSACTION)) {
        BitCouponCli.generateCreateTransaction(args[1], args[2]);
      } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
        BitCouponCli.generateSendTransaction(args[1], args[2], args[3], args[4]);
      } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
        BitCouponCli.verifyTransaction(args[1], args[2]);
      } else if (methodName.equalsIgnoreCase(GET_COUPONS)) {
        BitCouponCli.getCoupons(args[1], args[2]);
      } else if (methodName.equalsIgnoreCase(GET_COUPON_OWNERS)) {
        BitCouponCli.getCouponOwners(args[1], args[2], args[3]);
      } else if (methodName.equalsIgnoreCase(GENERATE_PRIVATE_KEY)) {
        BitCouponCli.generatePrivateKey();
      }
    }
  }

  /**
   * This method takes the name of the method, runs checkLength() to see if the number of arguments corresponds to what
   * the method needs, and returns true or false depending on whether it's correct or not. If it's incorrect, it will
   * print out an error message.
   */
  private static boolean isValidArgumentsLength(String methodName, int argsLength) {

    boolean correct = checkLength(methodName, argsLength);
    if (!correct) {
      String message = "Wrong number of arguments to method: " + methodName;
      System.err.println(message + methodName);
      return false;

    }
    return true;
  }

  /**
   * This method gets the name of a method and an integer representing the numbers of arguments sent as input to that
   * method, and checks that the number of arguments matches the number of arguments the method demands. It returns true
   * if it matches, and false if it doesn't.
   */
  private static boolean checkLength(String methodName, int argsLength) {
    int length = 0;
    if (methodName.equalsIgnoreCase(GENERATE_CREATE_TRANSACTION)) {
      length = 3;
    } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
      length = 5;
    } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
      length = 3;
    } else if (methodName.equalsIgnoreCase(GET_COUPONS)) {
      length = 3;
    } else if (methodName.equalsIgnoreCase(GET_COUPON_OWNERS)) {
      length = 4;
    } else if (methodName.equalsIgnoreCase(GENERATE_PRIVATE_KEY)) {
      length = 1;
    }
    return length == argsLength;
  }
}
