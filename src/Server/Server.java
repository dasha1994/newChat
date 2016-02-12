package Server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

  private static ServerSocket serverSocket = null;

  private static Socket clientSocket = null;
  private static final List<ClientThread> threads = new ArrayList();

  public static void main(String args[]) {

    int portNumber = 4444;
    System.out.println("Port number=" + portNumber);
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        threads.add(new ClientThread(clientSocket,threads));
        threads.get(threads.size()-1).start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}
