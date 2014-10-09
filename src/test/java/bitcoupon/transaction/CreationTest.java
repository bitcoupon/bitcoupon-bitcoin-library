package bitcoupon.transaction;

import junit.framework.TestCase;

public class CreationTest extends TestCase {

  private Creation creation;

  public void testConstructor() throws Exception {
    // TestConstructorXxx where Xxx is NullCreatorAddress, NegativeAmount etc.
    // Should throw illegalArgumentException
  }

  public void testConstructorWithNullCreatorAddress () throws Exception {
    try {
      creation = new Creation(null, 1);
      fail("Expected IllegalArgumentsException to be thrown for Creator Address being null");
    } catch (IllegalArgumentException creationAddressIsNullException){
      assertEquals(creationAddressIsNullException.getMessage(), "Creator Address can't be null!");
    }
  }

  public void testConstructorWithNegativeAmount() throws Exception {
    try {
      creation = new Creation("", -1);
      fail("Expected IllegalArgumentsException to be thrown for negative amount");
    } catch (IllegalArgumentException amountIsNegativeException){
      assertEquals(amountIsNegativeException.getMessage(), "Amount can't be negative!");
    }
  }

  public void testGetBytes() throws Exception {

  }

  public void testGetCreatorAddress() throws Exception {
    creation = new Creation("thisIsAnAddress", 1);
    assertEquals("thisIsAnAddress", creation.getCreatorAddress());
  }

  public void testGetAmount() throws Exception {
    creation = new Creation("", 1);
    assertEquals(1, creation.getAmount());
  }

  public void testGetSetSignature() throws Exception {
    creation = new Creation("", 1);
    assertNotNull(creation.getSignature());
    assertEquals("", creation.getSignature());

    creation.setSignature("newSignature");
    assertEquals("newSignature", creation.getSignature());
  }
}
