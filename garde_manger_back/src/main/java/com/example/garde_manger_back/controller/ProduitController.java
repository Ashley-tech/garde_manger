package com.example.garde_manger_back.controller;

import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.service.ProduitService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.garde_manger_back.dto.ProduitDTO;
import com.example.garde_manger_back.repository.ProduitRepository;

@RestController
@RequestMapping("/produits")
@CrossOrigin
public class ProduitController {

    private final ProduitService produitService;
    private ProduitRepository produitRepository;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<Produit> getProduits() {
        return produitService.getAllProduits();
    }

    @GetMapping("/{id}")
    public Produit getProduit(@PathVariable Integer id) {
        return produitService.getProduitById(id);
    }

    @PostMapping
    public Produit createProduit(@RequestBody Produit produit) {
        return produitService.createProduit(produit);
    }

    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Integer id) {
        produitService.deleteProduit(id);
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