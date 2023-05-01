package com.Accio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static Connection connection = null;

    public static Connection getConnection() {
        if(connection!=null){
            return connection;
        }
        String user = "root";
        String pwd = "264538";
        String db = "searchengineapp";
        return getConnection(user, pwd, db);
    }

    private static Connection getConnection(String user, String pwd, String db) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + db + "?user=" + user + "&password="+ pwd);

        } catch (ClassNotFoundException | SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
    }


}


//jdbc:mysql://localhost:3306/?user=root
//root@localhost:3306