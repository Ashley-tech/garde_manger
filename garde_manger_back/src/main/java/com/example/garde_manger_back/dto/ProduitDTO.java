package com.example.garde_manger_back.dto;

import java.time.LocalDate;

public class ProduitDTO {
    private String libelle;
    private String marque;
    private String type;
    private LocalDate dateConsommation;
    private LocalDate datePeremption;
    private String etat;

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
