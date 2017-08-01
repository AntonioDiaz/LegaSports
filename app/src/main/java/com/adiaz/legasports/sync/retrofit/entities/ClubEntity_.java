
package com.adiaz.legasports.sync.retrofit.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClubEntity_ {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contactPerson")
    @Expose
    private Object contactPerson;
    @SerializedName("contactEmail")
    @Expose
    private Object contactEmail;
    @SerializedName("contactAddress")
    @Expose
    private Object contactAddress;
    @SerializedName("contactPhone")
    @Expose
    private Object contactPhone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Object contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Object getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(Object contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Object getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(Object contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Object getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(Object contactPhone) {
        this.contactPhone = contactPhone;
    }

}
