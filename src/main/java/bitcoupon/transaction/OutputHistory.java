package bitcoupon.transaction;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.List;

public class OutputHistory {

  private final List<Output> outputList;

  public OutputHistory(List<Output> outputList) {
    this.outputList = outputList;
  }

  public List<Output> getOutputList() {
    return outputList;
  }

  public static OutputHistory fromJson(String outputHistoryJson) {
    return new Gson().fromJson(outputHistoryJson, OutputHistory.class);
  }

  public static OutputHistory fromJson(BufferedReader outputHistoryJson) {
    return new Gson().fromJson(outputHistoryJson, OutputHistory.class);
  }

  public static String toJson(OutputHistory outputHistory) {
    return new Gson().toJson(outputHistory, OutputHistory.class);
  }


  // This function is only for use in unit testing
  public void addTransactionToHistory(Transaction transaction) {
    for (Input input : transaction.getInputs()) {
      outputList.get((int) input.getReferredOutput() - 1).setReferringInput(1);
    }
    for (Output output : transaction.getOutputs()) {
      output.setOutputId(outputList.size() + 1);
      outputList.add(output);
    }
  }

}
