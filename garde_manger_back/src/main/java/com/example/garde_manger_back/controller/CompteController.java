package com.example.garde_manger_back.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.garde_manger_back.config.AuthService;
import com.example.garde_manger_back.dto.CompteDTO;
import com.example.garde_manger_back.dto.FindEmailRequest;
import com.example.garde_manger_back.dto.LoginResponse;
import com.example.garde_manger_back.entity.Compte;
import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.service.CompteService;

@RestController
@RequestMapping("/comptes")
@CrossOrigin
public class CompteController {
    private final CompteService compteService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public CompteController(CompteService compteService, PasswordEncoder passwordEncoder, AuthService authService) {
        this.compteService = compteService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
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

    @PostMapping("/forgot-password")
    public LoginResponse find(@RequestBody FindEmailRequest request) {
        return authService.find(request.email);
    }
}
