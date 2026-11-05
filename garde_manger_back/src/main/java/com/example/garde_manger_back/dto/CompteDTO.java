package com.example.garde_manger_back.dto;

import java.time.LocalDate;

public class CompteDTO {

    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private LocalDate dateNaissance;
    private String adresse;
    private String adresseComp;
    private String cp;
    private String ville;
    private String pays;
    private String fonction;

    public String getNom(){
        return nom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getMdp(){
        return mdp;
    }

    public void setMdp(String mdp){
        this.mdp = mdp;
    }

    public LocalDate getDateNaissance(){
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance){
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse(){
        return adresse;
    }

    public void setAdresse(String adresse){
        this.adresse = adresse;
    }

    public String getAdresseComp(){
        return adresseComp;
    }

    public void setAdresseComp(String adresseComp){
        this.adresseComp = adresseComp;
    }

    public String getCp(){
        return cp;
    }

    public void setCp(String cp){
        this.cp = cp;
    }

    public String getVille(){
        return ville;
    }

    public void setVille(String ville){
        this.ville = ville;
    }

    public String getPays(){
        return pays;
    }

    public void setPays(String pays){
        this.pays = pays;
    }

    public String getFonction(){
        return fonction;
    }

    public void setFonction(String fonction){
        this.fonction = fonction;
    }
}