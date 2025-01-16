package com.example.vehicleinsuranceclaim.entity;

public class NewUpdates {

    private int id;
    private String name;
    private String description;
    private String contact;

    public NewUpdates(int id, String name, String description, String contact) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
