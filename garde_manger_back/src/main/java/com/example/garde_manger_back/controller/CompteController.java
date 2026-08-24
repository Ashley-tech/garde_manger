package com.example.garde_manger_back.controller;

import com.example.garde_manger_back.entity.Compte;
import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.service.CompteService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.garde_manger_back.dto.CompteDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/comptes")
@CrossOrigin
public class CompteController {
    private final CompteService compteService;
    private final PasswordEncoder passwordEncoder;

    public CompteController(CompteService compteService, PasswordEncoder passwordEncoder) {
        this.compteService = compteService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping
    public List<Compte> getComptes() {
        return compteService.getAllComptes();
    }

    @GetMapping("/{id}")
    public Compte getCompte(@PathVariable Integer id) {
        return compteService.getCompteById(id);
    }

    @GetMapping("/{id}/produits")
    public List<Produit> getProduitsByCompte(@PathVariable Integer id) {
        return compteService.getProduitsByCompte(id);
    }

    @PostMapping
    public Compte createCompte(@RequestBody Compte compte){
        compte.setMdpCrypted(passwordEncoder.encode(compte.getMdp()));
        return compteService.createCompte(compte);
    }

    @PatchMapping("/{id}")
    public Compte modifierCompte(@PathVariable Integer id, @RequestBody CompteDTO dto) {
        return compteService.modifierCompte(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompte(@PathVariable Integer id){
        compteService.deleteCompte(id);
    }
}
