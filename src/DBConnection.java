import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static Connection connection;

    public static Connection getConnection(){

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/praktikum", "root", "thisISaPASSword");

//            ResultSet resultSet = statement.executeQuery("select * from weapons_trader");
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString("ITEM_NAME"));
//            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return connection;
    }

}
