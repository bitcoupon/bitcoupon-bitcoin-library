import java.io.*;
import java.net.*;

public class Json {
    public static void main(String[] args) {
        if ((args.length == 0) || (args.length > 1)) {
            System.err.println("Send exactly one JSON argument");
            return;
        }
        String json = args[0];

        System.out.println("Java lib back: " + json);
    }
}
