import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static JTextField enterField; // inputs message from user
    private static JTextArea displayArea; // display information to user
    private static ExecutorService executor; // will run players
    private ServerSocket server; // server socket
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private int counter = 1; // counter of number of connections
    private static int nClientsActive = 0;
    private static ArrayList<ClientHandler> connectedClients = new ArrayList<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);


    // set up GUI
    public Server() throws IOException {
        nClientsActive = 0;
        start();
    } // end Server constructor


    public static void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(23708);
            System.out.println("Chat Server (Server) is running on port " + 23708);

            while (true) {
                System.out.println("Over here");
                Socket clientSocket = serverSocket.accept();
                nClientsActive = nClientsActive + 1;
                System.out.println("In Here");
                ClientHandler client = new ClientHandler(new ObjectInputStream(clientSocket.getInputStream()), new ObjectOutputStream(clientSocket.getOutputStream()), clientSocket, nClientsActive);
                System.out.println("Client connected: " + nClientsActive);
                connectedClients.add(client);

                executorService.execute(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void broadcastFromServer(String message, int clientID) {
        Iterator<ClientHandler> clientIterator = connectedClients.iterator();
        while(clientIterator.hasNext()) {
            ClientHandler clientHandler = clientIterator.next();
            if(clientHandler.socketClosed()) {
                clientIterator.remove();
            }
            else {
                clientHandler.sendMessageFromServer(message);
            }
        }
        /*for (ClientHandler clientHandler : connectedClients) {
            System.out.println("Broadcasting to client: " + clientHandler.getClientID());
            clientHandler.sendMessageFromServer(message);
        }*/
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream clientReader;
        private ObjectOutputStream clientWriter;
        private final int clientID;

        public ClientHandler(ObjectInputStream clientReader, ObjectOutputStream clientWriter, Socket clientConnection, int ID) throws IOException {
            this.clientSocket = clientConnection;
            this.clientWriter = clientWriter;
            this.clientReader = clientReader;
            this.clientID = ID;
        }

        public int getClientID() { return clientID; }
        public ObjectOutputStream getOutputStream() { return clientWriter; }
        public boolean socketClosed() { return clientSocket.isClosed(); }

        @Override
        public void run() {
            try {
                //sendMessage(String.valueOf(clientID));
                while (true) {
                    String clientMessage = (String) clientReader.readObject();
                    if (clientMessage.equals("/disconnect")) {
                        // Client disconnected
                        connectedClients.remove(this);
                        break;
                    }

                    System.out.println("Received from client: " + clientMessage);
                    System.out.println("Connected client list size = " + connectedClients.size());
                    System.out.println();
                    //clientWriter.writeObject("clientWriter (Server): " + clientMessage);
                    //clientWriter.flush();

                    // Broadcast the message to all clients
//                    Server.broadcastFromServer(clientMessage);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        public static void sendMessage(String message) {
            for (ClientHandler client : connectedClients) {
                try {
                    ObjectOutputStream toUser = new ObjectOutputStream(client.getOutputStream());
                    toUser.writeObject(message);
                    toUser.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        public void sendMessageFromServer(String message) {
            try {
                clientWriter.writeObject(message);
                clientWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} // end class Server

