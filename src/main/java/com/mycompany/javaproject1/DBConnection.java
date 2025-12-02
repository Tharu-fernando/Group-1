

package com.mycompany.javaproject1;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ecofarm",
                    "root",
                    ""    // your XAMPP password (default empty)
            );
        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
            return null;
        }
    }
}
