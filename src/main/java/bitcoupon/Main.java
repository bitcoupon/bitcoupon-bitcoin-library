package bitcoupon;

public class Main {

  private static final String GENERATE_CREATION_TRANSACTION = "generateCreationTransaction";
  private static final String GENERATE_SEND_TRANSACTION = "generateSendTransaction";
  private static final String GET_CREATOR_ADDRESSES = "getCreatorAddresses";
  private static final String VERIFY_TRANSACTION = "verifyTransaction";


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
      System.out.println("Name: " + GENERATE_CREATION_TRANSACTION + " - Argumentss: String privateKey");
      System.out.println("Name: " + GENERATE_SEND_TRANSACTION
                         + " - Argumentss: String privateKey, String creatorAddress, String transactionHistoryJson, String receiverAddress");
      System.out.println(
          "Name: " + GET_CREATOR_ADDRESSES + " - Argumentss: String privateKey, String transactionHistoryJson");
      System.out.println(
          "Name: " + VERIFY_TRANSACTION + " - Argumentss: String transactionJson, String transactionHistoryJson");
    } else {

      String methodName = args[0];

      if (!isValidArgumentsLength(methodName, args.length)) {
        return;
      }

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

  /**
   * This method takes the name of the method, runs checkLength() to see if the number of arguments corresponds to what
   * the method needs, and returns true or false depending on whether it's correct or not. If it's incorrect, it will print out an error message.
   * @param methodName
   * @param argsLength
   * @return
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
   * This method gets the name of a method and an integer representing the numbers of arguments sent as input to that method,
   * and checks that the number of arguments matches the number of arguments the method demands. It returns true if it matches, and false if it doesn't.
   * 
   * @param methodName
   * @param argsLength
   * @return
   */
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
