package bitcoupon;

public class Input {

  private long inputId;
  private long outputId;
  private String scriptSig;

  Input(long inputId, long outputId) {
    this.inputId = inputId;
    this.outputId = outputId;
    this.scriptSig = "";
  }

}
