/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Santiago
 */

public class Adopter extends User {

    private Integer id;
    private String housingType;
    private Boolean hasOtherPets;

    public Adopter() {
    }

    public Adopter(Integer id, String housingType, Boolean hasOtherPets, String email, String password, String phoneNumber, String address) {
        super(email, password, phoneNumber, address);
        this.id = id;
        this.housingType = housingType;
        this.hasOtherPets = hasOtherPets;
    }

    public void searchPet() {
        System.out.println("Searching for a pet...");
    }

    public void requestAdoption() {
        System.out.println("Adoption request sent");
    }

    public void submitReview() {
        System.out.println("Review submitted");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHousingType() {
        return housingType;
    }

    public void setHousingType(String housingType) {
        this.housingType = housingType;
    }

    public Boolean getHasOtherPets() {
        return hasOtherPets;
    }

    public void setHasOtherPets(Boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
    }
}