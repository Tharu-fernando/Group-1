/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javaproject1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.KeyEvent; // For the Enter key
import javax.swing.JOptionPane;

import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Hansaka
 */
public class Farmer extends javax.swing.JInternalFrame {
    
    // 1. Database Config
    private final String DB_URL = "jdbc:mysql://localhost:3306/ecofarm";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = ""; 
    
    // To store the ID so we can find the correct farm details later
    private int currentFarmerID = -1;
    
    
     /**
     * Creates new form crop
     */
    public Farmer() {
        initComponents();
        
        // --- PASTE THIS NEW BLOCK HERE ---
        // 5. Notification Bell Action
        bell.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Change cursor to hand
        bell.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showNotifications(); // Call the method to show messages
            }
        });
        
        // 1. Remove Borders (Your original code)
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
        ui.setNorthPane(null);
        
        // 2. Add Logout Action (Your original code)
        logoutimg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutAction();
            }
        });

        // 3. NEW: Load Database Data instead of random generation
        loadFarmerAndFarmData();
        
        // 4. NEW: Add Enter Key Listener for Username Edit
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateUsername();
                }
            }
        });
    }
    
    
    
    // --- METHOD 3: Show Notifications from Database ---
    private void showNotifications() {
        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Fetch messages, newest first (Ordered by ID DESC)
            // Note: Using your specific spelling 'messege'
            String sql = "SELECT messege FROM notify_farmer ORDER BY ID DESC"; 
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            StringBuilder allMessages = new StringBuilder();
            int count = 0;
            
            while(rs.next()) {
                count++;
                String msg = rs.getString("messege");
                allMessages.append(count).append(". ").append(msg).append("\n\n");
            }
            
            con.close();
            
            if(count == 0) {
                JOptionPane.showMessageDialog(this, "No new notifications.");
            } else {
                // Use a TextArea inside the Popup so you can scroll if messages are long
                javax.swing.JTextArea textArea = new javax.swing.JTextArea(allMessages.toString());
                textArea.setRows(10);
                textArea.setColumns(40);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                
                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
                
                JOptionPane.showMessageDialog(this, scrollPane, "Farmer Notifications", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching messages: " + e.getMessage());
        }
    }
    
    
    private void logoutAction() {
    UserSession.clear(); // Clear saved session info

    new login().setVisible(true);  // Go back to login page
    this.dispose();                // Close Farmer page
}

    
    
    
    
  // --- METHOD 1: Load User & Farm Details from DB ---
    private void loadFarmerAndFarmData() {
        String currentUser = UserSession.getUsername(); // Get username from session
        
        // Fallback if testing without login
        if(currentUser == null || currentUser.isEmpty()) currentUser = "Hansaka"; 
        
        jTextField1.setText(currentUser);

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Step 1: Get Farmer ID
            String sqlFarmer = "SELECT ID FROM farmers WHERE username = ?";
            PreparedStatement pst1 = con.prepareStatement(sqlFarmer);
            pst1.setString(1, currentUser);
            ResultSet rs1 = pst1.executeQuery();
            
            if (rs1.next()) {
                currentFarmerID = rs1.getInt("ID");
            } else {
                return; // User not found
            }
            
            // Step 2: Get Farm Details using Farmer ID
            String sqlFarm = "SELECT location, size FROM farm WHERE farmer_id = ?";
            PreparedStatement pst2 = con.prepareStatement(sqlFarm);
            pst2.setInt(1, currentFarmerID);
            ResultSet rs2 = pst2.executeQuery();
            
            if (rs2.next()) {
                jLabel3.setText("Farm Location: " + rs2.getString("location"));
                jLabel5.setText("Farm Size: " + rs2.getString("size"));
            } else {
                jLabel3.setText("Farm Location: Not Assigned");
                jLabel5.setText("Farm Size: Not Assigned");
            }
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // --- METHOD 2: Update Username when Enter is pressed ---
    private void updateUsername() {
        String newName = jTextField1.getText().trim();
        String oldName = UserSession.getUsername();
        
        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "UPDATE farmers SET username = ? WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, newName);
            pst.setString(2, oldName);
            
            int rows = pst.executeUpdate();
            if(rows > 0) {
                JOptionPane.showMessageDialog(this, "Username updated!");
                UserSession.setUsername(newName); // Update session
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating: " + e.getMessage());
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        logoutimg = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bell = new javax.swing.JLabel();

        setBackground(new java.awt.Color(240, 151, 57));

        jPanel2.setBackground(new java.awt.Color(240, 151, 57));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/farmer 2.png"))); // NOI18N

        jTextField1.setFont(new java.awt.Font("SimSun", 1, 14)); // NOI18N
        jTextField1.setText("Farmer Name");

        logoutimg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logout (1).png"))); // NOI18N
        logoutimg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoutimgMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(logoutimg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(logoutimg, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(347, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(13, 36, 51));

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Assigned Farm Details");

        jLabel3.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Farm Location");

        jLabel5.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Farm Size");

        bell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bell (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(bell)))
                .addContainerGap(571, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(bell)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutimgMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutimgMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_logoutimgMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bell;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel logoutimg;
    // End of variables declaration//GEN-END:variables
}


