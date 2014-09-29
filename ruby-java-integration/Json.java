import java.io.*;
import java.net.*;

public class Json {
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
        Socket socket;
        InputStream in;
        OutputStream out;
        try {
            socket = new Socket("localhost", 2000);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            out.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(out));

            String json = rd.readLine();
            System.out.println("Client: " + json);

            wr.write("Json length: " + json.length());
            wr.flush();

            try {
                rd.close();
                wr.close();
                in.close();
                out.close();
                socket.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (IOException e)  {
            e.printStackTrace();
        }
    }
}
