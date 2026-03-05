/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Santiago
 */

public class Veterinarian extends User {

    private Integer id;
    private String licenseNumber;
    private String specialty;
    private String availability;

    public Veterinarian() {
    }

    public Veterinarian(Integer id, String licenseNumber, String specialty, String availability, String email, String password, String phoneNumber, String address) {
        super(email, password, phoneNumber, address);
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.specialty = specialty;
        this.availability = availability;
    }

    public void performCheckUp() {
        System.out.println("Performing check-up...");
    }

    public void updateMedicalRecord() {
        System.out.println("Medical record updated");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}