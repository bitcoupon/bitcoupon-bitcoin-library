package no.ntnu.bitcoupon.tests;

import android.test.InstrumentationTestCase;

/**
 * Created by Bjørn Bråthen on 23.09.14. This just test the test framework.
 */
public class ExampleTest extends InstrumentationTestCase {

  /**
   * Assure that 1 is not equal 5.
   */
  public void testOneNotEqualsFive() throws Exception {
    final int expected = 1;
    final int reality = 5;
    assertNotSame(expected, reality);
  }

  /**
   * Assure that 1 is actual 1.
   */
  public void testOneIsOne() throws Exception {
    assertEquals(1, 1);
  }
}