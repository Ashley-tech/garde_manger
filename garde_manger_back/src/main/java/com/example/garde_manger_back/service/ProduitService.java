package com.example.garde_manger_back.service;

import com.example.garde_manger_back.entity.Compte;
import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.repository.*;

import org.springframework.stereotype.Service;

import java.util.List;

import com.example.garde_manger_back.dto.ProduitDTO;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final CompteRepository compteRepository;

    public ProduitService(ProduitRepository produitRepository, CompteRepository compteRepository) {
        this.produitRepository = produitRepository;
        this.compteRepository = compteRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    public Produit createProduit(ProduitDTO dto) {
        Produit produit = new Produit();

        produit.setLibelle(dto.getLibelle());
        produit.setMarque(dto.getMarque());
        produit.setType(dto.getType());
        produit.setDateConsommation(dto.getDateConsommation());
        produit.setDatePeremption(dto.getDatePeremption());
        produit.setEtat(dto.getEtat());

        Compte compte = compteRepository.findById(dto.getProprietaireId())
                .orElseThrow(() ->
                        new RuntimeException("Compte introuvable")
                );

        produit.setProprietaire(compte);

        return produitRepository.save(produit);
    }

    public void deleteProduit(Integer id) {
        produitRepository.deleteById(id);
    }

    public Produit modifierProduit(Integer id,ProduitDTO dto) {

        Produit produit = produitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Produit introuvable")
                );

        if (dto.getLibelle() != null) {
            produit.setLibelle(dto.getLibelle());
        }

        if (dto.getMarque() != null) {
            produit.setMarque(dto.getMarque());
        }

        if (dto.getType() != null) {
            produit.setType(dto.getType());
        }

        if (dto.getDateConsommation() != null) {
            produit.setDateConsommation(dto.getDateConsommation());
        }

        if (dto.getDatePeremption() != null) {
            produit.setDatePeremption(dto.getDatePeremption());
        }

        if (dto.getEtat() != null) {
            produit.setEtat(dto.getEtat());
        }

        if (dto.getProprietaireId() != null) {
            Compte compte = compteRepository.findById(dto.getProprietaireId())
                        .orElseThrow(() ->
                                new RuntimeException("Compte introuvable")
                        );
            produit.setProprietaire(compte);
        }

        return produitRepository.save(produit);
    }
}