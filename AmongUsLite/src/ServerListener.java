import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener implements Runnable {
    private Socket socketListener;
    public ServerListener(Socket socketListener) {
        this.socketListener = socketListener;
    }

    /*@Override
    public void run() {
        try {
            ObjectInputStream serverReader = new ObjectInputStream(socketListener.getInputStream());
            while (true) {
                String serverMessage = (String) serverReader.readObject();
                Main.receiveMessage(serverMessage);
                System.out.println("Client (ServerListener): " + serverMessage);
            }
        }
        catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            System.out.println("Error (ServerListener)");
        }
    }*/
    @Override
    public void run() {
        try {
            System.out.println("Here (ServerListener)");
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socketListener.getInputStream()));
            System.out.println("Hereivyuenj (ServerListener)");
            while (true) {
                String serverMessage = serverReader.readLine();
                if (serverMessage == null) {
                    break;
                }
                Main.receiveMessage(serverMessage);
                System.out.println("Client (ServerListener): " + serverMessage);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Error (ServerListener)");
        }
    }
}
