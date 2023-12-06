import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

// The starting application for the game, runs the login screen prompting user for input
public class Start extends Application{
    // start method shows the login screen
    @Override
    public void start(Stage stage) throws Exception {
        // get the login fxml file
        Parent root =  FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
//        Parent root2 = FXMLLoader.load(getClass().getResource("WaitingScreen.fxml"));

        stage.setOnCloseRequest(windowEvent -> {

            System.exit(0);
//            stage.setScene(new Scene(root2));

        });

//        if (LoginScreen.getNumUsers() == 3) {
//            stage.setOnCloseRequest(windowEvent -> {
//                System.exit(0);
//            });
//        }

//        int connectedPlayers = 0;
//        if (connectedPlayers == 3) {
//            stage.setOnCloseRequest(windowEvent -> {
//                System.exit(0);
//                Client client = new Client();
//                client.start(new Stage());
//            });
//        }



    }
}
