package com.kiet.hostelmanagement;

public class HostelStudent {

    private int studentId;
    private String studentName;
    private String rollNumber;
    private String hostelName;
    private String roomNumber;
    private double fee;

    // Constructor
    public HostelStudent(int studentId, String studentName,
                         String rollNumber, String hostelName,
                         String roomNumber, double fee) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.hostelName = hostelName;
        this.roomNumber = roomNumber;
        this.fee = fee;
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getHostelName() {
        return hostelName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public double getFee() {
        return fee;
    }

    // Setters
    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Display student details
    @Override
    public String toString() {
        return "Student ID: " + studentId +
                "\nName: " + studentName +
                "\nRoll Number: " + rollNumber +
                "\nHostel: " + hostelName +
                "\nRoom: " + roomNumber +
                "\nFee: " + fee;
    }
}