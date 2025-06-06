package com.duoc.backend.Invoice;

import com.duoc.backend.Care.Care;
import com.duoc.backend.Medication.Medication;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String patientName;
    private LocalDate date;
    private LocalTime time;

    @ManyToMany
    @JoinTable(
        name = "invoice_cares",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "care_id")
    )
    private List<Care> cares;


    @ManyToMany
    @JoinTable(
        name = "invoice_medications",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private List<Medication> medications;


    private Double totalCost;

    public Invoice(Long id, String patientName, LocalDate date, List<Care> cares, List<Medication> medications) {
        this.id = id;
        this.patientName = patientName;
        this.date = date;
        this.cares = new ArrayList<>(cares);
        this.medications = new ArrayList<>(medications);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<Care> getCares() {
       return Collections.unmodifiableList(cares);
    }

    public void setCares(List<Care> cares) {
        this.cares = new ArrayList<>(cares);   
        
    }

    public List<Medication> getMedications() {
        return Collections.unmodifiableList(medications);
    }

    public void setMedications(List<Medication> medications) {
        this.medications = new ArrayList<>(medications);  
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}