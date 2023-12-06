import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ServerConnection {

    private static final int PORT = 23705;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static int i;
    private static boolean moreThanOnePlayer;
    private static final List<Position> clientPositions = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);
            System.out.println(InetAddress.getLocalHost());
            i = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler;
                if(i == 0){
                    clientHandler = new ClientHandler(clientSocket, true);
                    clients.add(clientHandler);
                    moreThanOnePlayer = false;
                    clients.get(i).sendMessage("IMPOSTOR");
                }
                else{
                    clientHandler = new ClientHandler(clientSocket, false);
                    clients.add(clientHandler);
                    moreThanOnePlayer = true;
                    clients.get(i).sendMessage("CREWMATE");
                }
                clients.get(i).setId(i);
                clients.get(i).sendMessage(Integer.toString(i));
                i++;
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //20
    //35
    private static void broadcastPositions() {
        StringBuilder positions = new StringBuilder();

        /*for(int i = 0; i < clients.size(); i++) {
            ClientHandler client = clients.get(i);
            clientPositions.get(i).updatePosition(client.getXPosition(), client.getYPosition());
            positions.append(i).append("&").append(clientPositions.get(i).getXCoordinate()).append("&").append(clientPositions.get(i).getYCoordinate()).append("&dead=").append(clients.get(i).isDead()).append(";");
        }*/

        for (ClientHandler client : clients) {
            positions.append(client.getId() + "&" + client.getName() + "&x=").append(client.getXPosition()).append("&y=").append(client.getYPosition()).append(";");
            if (!client.isDead()) {

            }
        }

        String positionsMessage = positions.toString();
        for (ClientHandler client : clients) {
            client.sendMessage(positionsMessage);
        }
    }

    private static void broadcastPositionsDeath(String tmpName, int tmpId) {
        StringBuilder positions = new StringBuilder();
        for (ClientHandler client : clients) {
            positions.append(client.getId() + "&" + client.getName() + "&x=").append(client.getXPosition()).append("&y=").append(client.getYPosition()).append("&dead=").append(client.isDead()).append(";");
        }

        String positionsMessage = positions.toString();
        for (ClientHandler client : clients) {
            client.sendMessage(positionsMessage);
        }
    }

    public static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private double xPosition = 0.0;
        private double yPosition = 0.0;
        private String name;
        private int id;
        private int tmpId;
        private String tmpName;
        private boolean isDead = false;
        private boolean isImpostor;
        private static final Map<Integer, DeadBody> deadBodies = new HashMap<>();

        public ClientHandler(Socket clientSocket, boolean isImpostor) throws IOException {
            while (clientPositions.size() <= id) {
                clientPositions.add(new Position(0, 0));
            }
            this.clientSocket = clientSocket;
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
            this.isImpostor = isImpostor;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead == -1) {
                        break; // client disconnected
                    }

                    String receivedMessage = new String(buffer, 0, bytesRead);
                    System.out.println("Received message from client: " + receivedMessage);

                    // Update client's position
                    String[] parts = receivedMessage.split("&");
                    if (parts.length >= 5) {
                        boolean requestKill = Boolean.parseBoolean(parts[4].substring(5));
                        name = parts[0];
                        xPosition = Double.parseDouble(parts[1].substring(2));
                        yPosition = Double.parseDouble(parts[2].substring(2));
                        isDead = Boolean.parseBoolean(parts[3].substring(5));
                        if (requestKill && !isImpostor) {
                            System.out.println("requestkill");
                            isDead = true;
                        }
                        System.out.println("before dead body check");
                        DeadBody none = new DeadBody(-999, -999);
                        deadBodies.put(0, none);
                        for (Map.Entry<Integer, DeadBody> entry : deadBodies.entrySet()) {
                            System.out.println("Dead body check");
                            if (isPlayerInRange(xPosition, yPosition, entry.getValue().getX(), entry.getValue().getY())) {
                                entry.getValue().addVisiblePlayerIDs(id + 1);
                            }
                        }
                        System.out.println("after dead body check");

                        //clientPositions.get(id).updatePosition(xPosition, yPosition);
                    }
                    /*if (parts.length == 2) {
                        tmpName = parts[0];
                        tmpId = Integer.parseInt(parts[1].substring(2));
                        broadcastPositionsDeath(tmpName, tmpId);
                    }*/

                    // Broadcast all positions to clients
                    broadcastPositions();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Remove client when it disconnects
                clients.remove(this);
                System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
                i--;
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Broadcast updated positions when a client disconnects
                broadcastPositions();
            }
        }

        private boolean isPlayerInRange(double playerX, double playerY, double deadBodyX, double deadBodyY) {
            double distance = Math.sqrt(Math.pow(playerX - deadBodyX, 2) + Math.pow(playerY - deadBodyY, 2));
            System.out.println("DISTANCE + " + distance);
            return distance <= 210;
        }

        public boolean isDead() {
            return isDead;
        }

        public void killHandle(String fileName, int clientID) {
            ClientHandler killedCrewmate = clients.get(clientID);
            killedCrewmate.isDead = true;
            broadcastPositions();
        }

        public void sendMessage(String message) {
            try {
                outputStream.write(message.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public double getXPosition() {
            return xPosition;
        }

        public double getYPosition() {
            return yPosition;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}