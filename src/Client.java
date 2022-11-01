import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
  private Socket client;

  public Client() {
    try {
      // create a client socket connected to the server
      System.out.println("Connecting to server...");
      client = new Socket("localhost", 8080);
    } catch (IOException e) {
      System.out.println("Could not connect to server");
      System.exit(-1);
    }
  }

  public void start() {
    // listen for input from the user
    new Thread(() -> {
      while (true) {
        try {
          // receive ping
          byte[] buffer = new byte[1024];
          int read = client.getInputStream().read(buffer);
          String ping = new String(buffer, 0, read);
          System.out.println("Received: " + ping);
          if (ping.equals("ping")) {
            System.out.println("Server is alive");
            client.getOutputStream().write("pong".getBytes());
            client.getOutputStream().flush();
          }
        } catch (IOException e) {
          System.out.println("Could not read from user");
          System.exit(-1);
        }
      }
    }).start();
  }
}
