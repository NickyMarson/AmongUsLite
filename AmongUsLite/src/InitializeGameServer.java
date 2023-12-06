// Fig. 27.6: ServerTest.java
// Test the Server application.

import java.io.IOException;

public class InitializeGameServer {
    public static void main(String[] args) throws IOException {
//        Server application = new Server(); // create server
        Server.start(); // run server application
    } // end main
} // end class ServerTest
