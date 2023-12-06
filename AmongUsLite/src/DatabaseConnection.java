import java.sql.*;
//import com.sql.cj.jdbc.Driver;


// The databsae connection class establishes a connection to the sql database
public class DatabaseConnection {
    // database name
    private String databaseName = "engr_class007";
    // database username
    private final String username = "engr_class007";
    // database password
    private final String password = "i4iOrfD0XLDJuchp";
    // connection variable to the database
    private Connection connection;

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
////        try {
////            Class.forName("com.mysql.cj.jdbc.Driver");
////        } catch (ClassNotFoundException ex)
////        {
////            System.out.println(ex.getMessage());
////        }
////        Class.forName("com.mysql.jdbc.Driver");
//        try {
//            DatabaseConnection connection1 = new DatabaseConnection();
//            connection1.insertNewUser("Cavan", "password");
//        }
//        catch (SQLException ex)
//        {
//            System.out.println(ex.getMessage());
//        }
//
//    }
    // Constructor for the database establishes a connection to the sql database allowing for queries
    public DatabaseConnection() {
        String url = "jdbc:mysql://s-l112.engr.uiowa.edu/" + databaseName + "?enabledTLSProtocols=TLSv1.2";
        try
        {
            this.connection  = DriverManager.getConnection(url, username, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // The insertNewUser method allows users to create new users for the game, making a new row in the user Table
    public int insertNewUser(String user, String password) throws SQLException
    {
        // is user already exists, delete
        PreparedStatement eraseOldData = connection.prepareStatement("DELETE FROM user WHERE username = '" + user + "'");
        eraseOldData.execute();
        eraseOldData.close();

        // actual statement for putting values into table
        PreparedStatement insertUser = connection.prepareStatement("INSERT INTO user " + "(username, password) " + "VALUES(?, ?)");
        int result = -1;
        try {
            insertUser.setString(1, user);
            insertUser.setString(2, password);

            // insert the new entry; returns # of rows updated
            result = insertUser.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

//        PreparedStatement insert = connection.prepareStatement("INSERT INTO user VALUES('" + user + "', '" + password + "')");
//        insert.execute();
        return result;
    }


    // getUserFromDB returns a User object from data from the user table
    public User getUserFromDB(String user) throws SQLException {
        // create query
        Statement request = connection.createStatement();
        ResultSet query = request.executeQuery("SELECT * FROM user WHERE username = '" + user + "'");

        // initialize null values in case the information doesnt exist
        String username = null;
        String password = null;
        int games_played = -12345;
        int wins = -12345;
        int losses = -12345;


        // gets information from query
        while (query.next())
        {
            username = query.getString("username");
            password = query.getString("password");
            games_played = query.getInt("games_played");
            wins = query.getInt("wins");
            losses = query.getInt("losses");
        }

//        return new User(username, password, games_played, wins, losses);
        return new User(username, password, games_played, wins, losses);
    }

    // updateDBCol allows updates to specific values in columns, for examples wins and losses
    public void updateDBCol(String user, String column_name, Integer new_value) throws SQLException {
        try (PreparedStatement update_col = connection.prepareStatement("UPDATE user SET " + column_name + " = ? WHERE username = " + user))
        {
            update_col.setInt(1, new_value);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    // matchPassword method returns a boolean if the password entered in the login screen matches that which is attributed
    // to the username from the database
    public boolean matchPassword(String username, String password) throws SQLException {
        // query string
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        // set the values in the query
        try(PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, username);
            statement.setString(2, password);
            // execute query
            try (ResultSet set = statement.executeQuery())
            {
                return set.next();
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }

    }

    // userExists method returns a boolean for whether the user exists in the database already
    public boolean userExists(String username) throws SQLException {
        // query string
        String query = "SELECT COUNT(*) as user_count FROM user WHERE username = ?";

        // prepare query with username information
        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            // set value in query
            statement.setString(1, username);

            // execute query
            try (ResultSet set = statement.executeQuery())
            {
                if (set.next())
                {
                    // if the count of usernames is 0, return false
                    int count = set.getInt("user_count");
                    return count > 0;
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
        return false;
    }


}
