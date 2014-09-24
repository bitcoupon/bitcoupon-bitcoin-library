package bitcoupon;

public class Creation {

  private long creationId;
  private String scriptPubKey;
  private String subType;
  private int amount;
  private String scriptSig;

  Creation(long creationId, String scriptPubKey, String subType, int amount) {
    this.creationId = creationId;
    this.scriptPubKey = scriptPubKey;
    this.subType = subType;
    this.amount = amount;
    this.scriptSig = "";
  }

}
