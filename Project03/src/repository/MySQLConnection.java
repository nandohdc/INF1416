package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static int status = 0;

    public static Connection getMySQLConnection(){
        Connection connection;
        try {
            String driverName = "org.sqlite.JDBC";
            Class.forName(driverName);
            String url = "jdbc:sqlite:database.sqlite3";
            connection = DriverManager.getConnection(url);
            if (connection != null)
                status = 1;
            else
                status = 0;

            return connection;
        } catch (ClassNotFoundException e) {  //Driver não encontrado
            System.out.println("O driver expecificado nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao Banco de Dados.");
            return null;
        }
    }

    public static int statusConnection() {
        return status;
    }

    public static boolean closeConnection() {
        try {
            MySQLConnection.getMySQLConnection().close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
