package com.kiet.hostelmanagement;

import java.sql.*;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== KIET HOSTEL MANAGEMENT SYSTEM =====");
            System.out.println("1. Insert Hostel Student");
            System.out.println("2. Retrieve Students by Hostel");
            System.out.println("3. Update Hostel Fee");
            System.out.println("4. Search and Modify Room Details");
            System.out.println("5. Display Total Fee and Delete Student");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    insertStudent();
                    break;

                case 2:
                    retrieveStudents();
                    break;

                case 3:
                    updateFee();
                    break;

                case 4:
                    updateRoom();
                    break;

                case 5:
                    aggregateAndDelete();
                    break;

                case 6:
                    System.out.println("Thank you for using Hostel Management System!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }

    // 1. Insert Student Records
    static void insertStudent() {

        String sql = "INSERT INTO HostelStudent VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Roll Number: ");
            String roll = sc.nextLine();

            System.out.print("Enter Hostel Name: ");
            String hostel = sc.nextLine();

            System.out.print("Enter Room Number: ");
            String room = sc.nextLine();

            System.out.print("Enter Hostel Fee: ");
            double fee = Double.parseDouble(sc.nextLine());

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, roll);
            ps.setString(4, hostel);
            ps.setString(5, room);
            ps.setDouble(6, fee);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student inserted successfully!");
            }

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                System.out.println("Duplicate Student ID! Record already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // 2. Retrieve Students by Hostel and Fee
    static void retrieveStudents() {

        String sql = "SELECT * FROM HostelStudent " +
                "WHERE hostel_name = ? AND fee > 50000 " +
                "ORDER BY fee DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Hostel Name: ");
            String hostel = sc.nextLine();

            ps.setString(1, hostel);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("\nStudent ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Roll Number: " + rs.getString("roll_number"));
                System.out.println("Hostel: " + rs.getString("hostel_name"));
                System.out.println("Room: " + rs.getString("room_number"));
                System.out.println("Fee: " + rs.getDouble("fee"));
            }

            if (!found) {
                System.out.println("No matching students found.");
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Update Hostel Fee
    static void updateFee() {

        String sql = "UPDATE HostelStudent SET fee = ? WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Hostel Fee: ");
            double fee = Double.parseDouble(sc.nextLine());

            ps.setDouble(1, fee);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Hostel fee updated successfully!");
            } else {
                System.out.println("Student record does not exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Search and Modify Room Details
    static void updateRoom() {

        String selectSQL = "SELECT * FROM HostelStudent WHERE student_id = ?";
        String updateSQL = "UPDATE HostelStudent SET room_number = ? WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement selectPS = con.prepareStatement(selectSQL)) {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            selectPS.setInt(1, id);

            ResultSet rs = selectPS.executeQuery();

            if (rs.next()) {

                System.out.println("\nStudent Found!");
                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Current Room: " + rs.getString("room_number"));

                System.out.print("Enter New Room Number: ");
                String room = sc.nextLine();

                try (PreparedStatement updatePS = con.prepareStatement(updateSQL)) {

                    updatePS.setString(1, room);
                    updatePS.setInt(2, id);

                    updatePS.executeUpdate();

                }

                System.out.println("\nRoom updated successfully!");

                try (PreparedStatement ps = con.prepareStatement(selectSQL)) {

                    ps.setInt(1, id);

                    ResultSet updatedRS = ps.executeQuery();

                    if (updatedRS.next()) {

                        HostelStudent student = new HostelStudent(
                                updatedRS.getInt("student_id"),
                                updatedRS.getString("student_name"),
                                updatedRS.getString("roll_number"),
                                updatedRS.getString("hostel_name"),
                                updatedRS.getString("room_number"),
                                updatedRS.getDouble("fee")
                        );

                        System.out.println("\nUpdated Student Details:");
                        System.out.println(student);
                    }

                    updatedRS.close();
                }

            } else {
                System.out.println("Student not found!");
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Aggregate Query and Conditional Deletion
    static void aggregateAndDelete() {

        String sumSQL = "SELECT SUM(fee) AS total_fee FROM HostelStudent";

        String deleteSQL = "DELETE FROM HostelStudent WHERE student_id = ? AND fee > 0";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sumSQL);

            if (rs.next()) {
                System.out.println("\nTotal Hostel Fee Collected: ₹" +
                        rs.getDouble("total_fee"));
            }

            rs.close();

            try (PreparedStatement ps = con.prepareStatement(deleteSQL)) {

                System.out.print("\nEnter Student ID to Delete: ");
                int id = Integer.parseInt(sc.nextLine());

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Student deleted successfully!");
                } else {
                    System.out.println("Student cannot be deleted. Record does not exist or fee is zero.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}