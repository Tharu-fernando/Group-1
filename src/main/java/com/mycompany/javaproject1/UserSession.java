/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject1;

/**
 *
 * @author Hansaka
 */
public class UserSession {
    
    
    private static String username;
    private static int farmerId;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void setFarmerId(int id) {
        farmerId = id;
    }

    public static int getFarmerId() {
        return farmerId;
    }

    public static void clear() {
        username = null;
        farmerId = 0;
    }
    
    
    
}
