package com.example.garde_manger_back.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String libelle;

    @Column(length = 50)
    private String marque;

    @Column(length = 50)
    private String type;

    @Column(name = "date_consommation")
    private LocalDate dateConsommation;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @Column(nullable = false, length = 50)
    private String etat;

    @ManyToOne
    @JoinColumn(name = "proprietaire")
    private Compte proprietaire;

    public Integer getId(){
        return id;
    }

    public Compte getProprietaire(){
        return proprietaire;
    }

    public String getLibelle(){
        return libelle;
    }

    public void setLibelle(String libelle){
        this.libelle = libelle;
    }

    public String getMarque(){
        return marque;
    }

    public void setMarque(String marque){
        this.marque = marque;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public LocalDate getDateConsommation(){
        return dateConsommation;
    }

    public void setDateConsommation(LocalDate dateConsommation){
        this.dateConsommation = dateConsommation;
    }

    public LocalDate getDatePeremption(){
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption){
        this.datePeremption = datePeremption;
    }

    public String getEtat(){
        return etat;
    }

    public void setEtat(String etat){
        this.etat = etat;
    }
}