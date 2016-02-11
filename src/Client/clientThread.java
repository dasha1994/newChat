package Client;
import db.UsersService;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class clientThread extends Thread{
  private String clientName = null;
  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private final clientThread[] threads;
  private int maxClientsCount;

  public clientThread(Socket clientSocket, clientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxClientsCount = threads.length;
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    clientThread[] threads = this.threads;

    try {

      UsersService us = new UsersService();
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
      String name;
      while (true) {
        os.println("Enter your name.");
        name = is.readLine().trim();
        if (us.isUserExist(name)) {
          if (us.isBanned(name)) {
            os.println("This user is banned");
            continue;
          } else
            break;
        } else {
          us.insert(name);
          break;
        }
      }
      os.println("Welcome " + name
              + " to chat.\nTo leave enter /quit in a new line.\n"+
              "You cannot send messages that contain \"con\" .");
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] == this) {
            clientName = "@" + name;
            break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].os.println("A new user " + name
                    + " joined the chat");
          }
        }
      }
      while (true) {
        String line = is.readLine();
        if (line.startsWith("/quit")) {
          break;
        }
        synchronized (this) {
          for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i].clientName != null) {
              threads[i].os.println("<" + name + "> " + line);
              if(!us.isCorrectMsg(line)) {
                us.update(name);
                os.println("You are banned!");
                threads[i]=null;
              }
            }
          }
        }
      }
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this
                  && threads[i].clientName != null) {
            threads[i].os.println("The user " + name
                    + " is leaving the chat");
          }
        }
      }
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] == this) {
            threads[i] = null;
          }
        }
      }
      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}
