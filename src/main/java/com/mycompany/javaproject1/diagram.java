/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*; // Import SQL for Database Connection
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Hansaka
 */
public class diagram extends JPanel {
    
    // Database Connection Variables
    private final String DB_URL = "jdbc:mysql://localhost:3306/ecofarm";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = ""; // <--- WRITE YOUR SQL PASSWORD HERE

    public diagram() {
        setLayout(new BorderLayout());

        // --- Main Panel to hold everything ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1)); // 2 Rows

        // --- TOP ROW (Bar, Line, Pie) ---
        JPanel topRow = new JPanel(new GridLayout(1, 3));
        topRow.add(createBarChartPanel());
        topRow.add(createLineChartPanel());
        topRow.add(createPieChartPanel());

        // --- BOTTOM ROW (Table, Histogram) ---
        JPanel bottomRow = new JPanel(new GridLayout(1, 2));
        bottomRow.add(createTablePanel());
        bottomRow.add(createHistogramPanel());

        mainPanel.add(topRow);
        mainPanel.add(bottomRow);

        add(mainPanel, BorderLayout.CENTER);
    }

    // --- 1. BAR CHART (Connected to 'organics' table) ---
    private JPanel createBarChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = con.createStatement();
            String sql = "SELECT monthly, kg FROM organics";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String month = rs.getString("monthly");
                int kg = rs.getInt("kg");
                dataset.addValue(kg, "Amount", month);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching Organics data: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Remaining Organics Lvl", "monthly", "amount (KG)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(220, 50, 60)); 
        plot.setBackgroundPaint(Color.WHITE);
        return new ChartPanel(chart);
    }

    // --- 2. LINE CHART (Connected to 'fertilizers' table) ---
    private JPanel createLineChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = con.createStatement();
            String sql = "SELECT monthly, amount FROM fertilizers";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String month = rs.getString("monthly");
                int amount = rs.getInt("amount");
                dataset.addValue(amount, "Amount", month);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching Fertilizers data: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "fertilizers", "monthly", "amount",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(200, 100, 100));
        plot.setBackgroundPaint(Color.WHITE);
        return new ChartPanel(chart);
    }

    // --- 3. PIE CHART (Connected to 'seed' table) ---
    private JPanel createPieChartPanel() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = con.createStatement();
            String sql = "SELECT name, percentage FROM seed";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String name = rs.getString("name");
                int percentage = rs.getInt("percentage");
                dataset.setValue(name, percentage);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching Seed data: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createPieChart3D(
                "seeds", dataset, true, true, false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setForegroundAlpha(0.60f);
        plot.setBackgroundPaint(Color.WHITE);
        return new ChartPanel(chart);
    }

    // --- 4. TABLE (Connected to 'equipment' table) ---
    private JPanel createTablePanel() {
        // Define Column Names
        String[] columns = {"User Id", "User Name", "Email", "Equipment"};
        DefaultTableModel model = new DefaultTableModel(columns, 0); // 0 rows initially

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = con.createStatement();
            String sql = "SELECT ID, farmer_name, email, equipment FROM equipment";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ID"));
                row.add(rs.getString("farmer_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("equipment"));
                model.addRow(row);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching Equipment data: " + e.getMessage());
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setGridColor(new Color(220, 50, 60));
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(220, 50, 60));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Keep the style where row index 2 is red (if it exists), otherwise just text styling
                if (row == 2) {
                    c.setBackground(new Color(220, 50, 60));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(new Color(220, 50, 60));
                }
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane);
        return panel;
    }

    // --- 5. HISTOGRAM (Not Connected - Random Data) ---
    private JPanel createHistogramPanel() {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        double[] values = new double[100];
        Random generator = new Random();
        for (int i = 0; i < 100; i++) {
            values[i] = generator.nextDouble() * 100;
        }
        dataset.addSeries("Histogram", values, 15);

        JFreeChart chart = ChartFactory.createHistogram(
                "Farmers XP", "Farmers", "Years",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(255, 100, 100));
        return new ChartPanel(chart);
    }
    
    // Main method tailored for testing the panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Testing Diagram Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new diagram()); // Add the panel to the frame
            frame.setSize(1200, 800);
            frame.setVisible(true);
        });
    }
}