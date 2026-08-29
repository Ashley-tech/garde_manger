package com.example.garde_manger_back.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "compte")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(nullable = false, unique = true, length = 70)
    private String email;

    @Column(nullable = false, length = 50)
    private String mdp;

    @Column(name = "mdp_crypted", nullable = false, length = 500)
    private String mdpCrypted;

    @Column(name = "date_naissance",nullable = false)
    private LocalDate dateNaissance;

    @Column(length = 80)
    private String adresse;

    @Column(name="adresse_comp",length = 50)
    private String adresseComp;

    @Column(length = 15)
    private String cp;

    @Column(length = 50)
    private String ville;

    @Column(length = 50)
    private String pays;

    @Column(columnDefinition = "TEXT")
    private String fonction;

    @OneToMany(mappedBy = "proprietaire")
    @JsonIgnore
    private List<Produit> produits;

    public Integer getId(){
        return id;
    }

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

    public String getMdpCrypted(){
        return mdpCrypted;
    }

    public void setMdpCrypted(String mdpCrypted){
        this.mdpCrypted = mdpCrypted;
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

    public List<Produit> getProduits(){
        return produits;
    }
}