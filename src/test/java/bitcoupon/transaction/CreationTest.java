package bitcoupon.transaction;

import junit.framework.TestCase;

public class CreationTest extends TestCase {

  public void testConstructor() throws Exception {
    // TestConstructorXxx where Xxx is NullCreatorAddress, NegativeAmount etc.
    // Should throw illegalArgumentException
  }

  public void testGetBytes() throws Exception {

  }

  public void testGetCreatorAddress() throws Exception {

  }

  public void testGetAmount() throws Exception {

  }

  public void testGetSetSignature() throws Exception {
    Creation creation = new Creation("", 1);
    assertNotNull(creation.getSignature());
    assertEquals("", creation.getSignature());

    creation.setSignature("newSignature");
    assertEquals("newSignature", creation.getSignature());
  }
}
