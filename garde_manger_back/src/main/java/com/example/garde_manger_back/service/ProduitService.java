package com.example.garde_manger_back.service;

import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.repository.ProduitRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import com.example.garde_manger_back.dto.ProduitDTO;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    public Produit createProduit(Produit produit) {
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

        return produitRepository.save(produit);
    }
}