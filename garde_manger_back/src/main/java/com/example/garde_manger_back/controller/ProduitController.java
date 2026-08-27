package com.example.garde_manger_back.controller;

import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.service.ProduitService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.garde_manger_back.dto.ProduitDTO;

@RestController
@RequestMapping("/produits")
@CrossOrigin
public class ProduitController {

    private final ProduitService produitService;

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
    public Produit createProduit(@RequestBody ProduitDTO produit) {
        return produitService.createProduit(produit);
    }

    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Integer id) {
        produitService.deleteProduit(id);
    }

    @PatchMapping("/{id}")
    public Produit modifierProduit(@PathVariable Integer id,@RequestBody ProduitDTO dto) {
        return produitService.modifierProduit(id,dto);
    }
}