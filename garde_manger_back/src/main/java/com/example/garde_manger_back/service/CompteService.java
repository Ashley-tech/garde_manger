package com.example.garde_manger_back.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.garde_manger_back.dto.CompteDTO;
import com.example.garde_manger_back.entity.Compte;
import com.example.garde_manger_back.entity.Produit;
import com.example.garde_manger_back.repository.CompteRepository;
import com.example.garde_manger_back.repository.ProduitRepository;

@Service
public class CompteService {

    private final CompteRepository compteRepository;
    private final ProduitRepository produitRepository;
    private final PasswordEncoder passwordEncoder;

    public CompteService(CompteRepository compteRepository, ProduitRepository produitRepository,PasswordEncoder passwordEncoder) {
        this.compteRepository = compteRepository;
        this.produitRepository = produitRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Produit> getProduitsByCompte(Integer compteId) {
        if (!compteRepository.existsById(compteId)) {
            throw new RuntimeException("Compte introuvable");
        }
        return produitRepository.findByProprietaireId(compteId);
    }

    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Compte getCompteById(Integer id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
    }

    public Compte createCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    public void deleteCompte(Integer id) {
        if (!compteRepository.existsById(id)) {
            throw new RuntimeException("Compte introuvable");
        }

        produitRepository.deleteByProprietaireId(id);

        compteRepository.deleteById(id);
    }

    public Compte modifierCompte(Integer id,CompteDTO dto) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Compte introuvable")
                );

        if (dto.getNom() != null) {
            compte.setNom(dto.getNom());
        }

        if (dto.getPrenom() != null) {
            compte.setPrenom(dto.getPrenom());
        }

        if (dto.getEmail() != null) {
            compte.setEmail(dto.getEmail());
        }

        if (dto.getMdp() != null && !dto.getMdp().isBlank()){
            compte.setMdp(dto.getMdp());
            compte.setMdpCrypted(passwordEncoder.encode(dto.getMdp()));
        }

        if (dto.getAdresse() != null) {
            compte.setAdresse(dto.getAdresse());
        }

        if (dto.getAdresseComp() != null) {
            compte.setAdresseComp(dto.getAdresseComp());
        }

        if (dto.getCp() != null) {
            compte.setCp(dto.getCp());
        }

        if (dto.getVille() != null) {
            compte.setVille(dto.getVille());
        }

        if (dto.getPays() != null) {
            compte.setPays(dto.getPays());
        }

        if (dto.getFonction() != null) {
            compte.setFonction(dto.getFonction());
        }

        return compteRepository.save(compte);
    }

    public Compte connexion(String email, String motDePasse) {

        Compte compte = compteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Email ou mot de passe incorrect")
                );

        if (!passwordEncoder.matches(
                motDePasse,
                compte.getMdpCrypted()
        )) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        return compte;
    }
}