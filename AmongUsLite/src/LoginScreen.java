import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

// LoginScreen is the main driver for the login fxml file
public class LoginScreen {
    // creates database connection
    DatabaseConnection connection = new DatabaseConnection();
    @FXML
    private TextField username_field;
    @FXML
    private TextField password_field;
    @FXML
    private Button login_button;
    @FXML
    private Button create_button;

    public String username;
    public String password;
    public volatile int numUsers;

    // function for logging in user by pressing login button
    @FXML
    public void loginUser(javafx.event.ActionEvent actionEvent) throws Exception {
        username = username_field.getText();
        password = password_field.getText();
//        System.out.println("Login Pressed");
        // if the passwords and usernames match, the user exists and application starts
        if (connection.matchPassword(username, password)) {
            // Implement Waiting screen... cant do, not enough knowledge of javafx
//            numUsers++;
//            System.out.println("LOGIN " + numUsers);
//            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
//            ServerConnection.numUsers += 1;
//            System.out.println("SERVER " + ServerConnection.numUsers);
//            WaitingScreen.numClients++;
//
//            WaitingScreen waitingScreen = new WaitingScreen();
//            waitingScreen.start(new Stage());
            // close window and starts stage
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            Client client = new Client();
            client.start(new Stage());
        } else {
//            System.out.println("Invalid");
            // else prints to login screen
            username_field.setText("Invalid Login");
            username_field.setText("Invalid Login");
        }
    }


    // creates user if create button is pressed
    @FXML
    public void createUser(javafx.event.ActionEvent actionEvent) throws Exception {
        username = username_field.getText();
        password = password_field.getText();

        // if user doesnt exist then the user is created
        if (!connection.userExists(username))
        {
            connection.insertNewUser(username, password);
            ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
            Client client = new Client();
            client.start(new Stage());
//            LoginScreen.numClients++;

//            WaitingScreen waitingScreen = new WaitingScreen();
//            waitingScreen.start(new Stage());
            // Implement waiting screen
        }
        else{
            // sets text to user already exists if user exists in database
            username_field.setText("User already exits");
            password_field.setText("User already exists");
        }
//        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
//        Client client = new Client();
//        client.start(new Stage());
    }


}
