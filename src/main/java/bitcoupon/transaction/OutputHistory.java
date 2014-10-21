package bitcoupon.transaction;

import com.google.gson.Gson;

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

  public static String toJson(OutputHistory outputHistory) {
    return new Gson().toJson(outputHistory, OutputHistory.class);
  }

}
