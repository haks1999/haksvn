package com.haks.swateam.testp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name="person")
public class Person {
     
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int pid;
     
    private String firstname;
     
    private String lastname;
 
    public int getPid() {
        return pid;
    }
 
    public void setPid(int pid) {
        this.pid = pid;
    }
 
    public String getFirstname() {
        return firstname;
    }
 
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
 
    public String getLastname() {
        return lastname;
    }
 
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
