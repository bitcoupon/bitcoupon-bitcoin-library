package bitcoupon.transaction;

import junit.framework.TestCase;

import java.util.ArrayList;

public class OutputTest extends TestCase {

  public void testGetBytes() throws Exception {
    ArrayList<byte[]> bytes = new ArrayList<>();
    ArrayList<byte[]> correctBytes = new ArrayList<>();
    bytes.add(new Output("creatorAddress", "payload", 1, "receiverAddress").getBytes());
    correctBytes.add(new byte[]{0,0,0,14,99,114,101,97,116,111,114,65,100,100,114,101,115,115,0,0,0,7,112,97,121,108,111,97,100,0,0,0,1,0,0,0,15,114,101,99,101,105,118,101,114,65,100,100,114,101,115,115});
    for (int i = 0; i < bytes.size(); i++) {
      assertEquals(correctBytes.get(i).length, bytes.get(i).length);
      for (int j = 0; j < bytes.get(i).length; j++) {
        assertEquals(correctBytes.get(i)[j], bytes.get(i)[j]);
      }
    }
  }

}