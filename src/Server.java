import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
  // create a server socket
  private ServerSocket server;
  // store the client sockets
  List<Socket> clients = new ArrayList<>();

  public Server() {
    try {
      // create a server socket on port 8080
      System.out.println("Starting server...");
      server = new ServerSocket(8080);
    } catch (IOException e) {
      System.out.println("Could not listen on port: 8080");
      System.exit(-1);
    }
  }

  public void start() {
    // listen for client connections
    new Thread(() -> {
      System.out.println("Waiting for clients...");
      while (true) {
        try {
          // accept a client connection
          Socket client = server.accept();
          clients.add(client);
          System.out.println("Client connected");
          // create a new thread to handle the client
          System.out.println("Starting new client thread");
          new Thread(new ClientHandler(client)).start();
        } catch (IOException e) {
          System.out.println("Accept failed: 8080");
          System.exit(-1);
        }
      }
    }).start();
  }

  private class ClientHandler implements Runnable {
    private Socket client;
    public ClientHandler(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {
      System.out.println("Client handler started");
      while (true) {
        try {
          client.getOutputStream().write("ping".getBytes());

          // read a message from the client
          byte[] buffer = new byte[1024];
          int read = client.getInputStream().read(buffer);
          String message = new String(buffer, 0, read);
          System.out.println("Received: " + message);
          if (message.equals("pong")) {
            System.out.println("Client is alive");
          }

        } catch (IOException e) {
          System.out.println("Read failed");
          System.exit(-1);
        }
      }
    }
  }
}
