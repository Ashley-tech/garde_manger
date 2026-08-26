package com.example.garde_manger_back.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.garde_manger_back.dto.LoginResponse;
import com.example.garde_manger_back.entity.Compte;
import com.example.garde_manger_back.repository.CompteRepository;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final CompteRepository compteRepository;

    public AuthService(PasswordEncoder passwordEncoder,
                       CompteRepository compteRepository) {
        this.passwordEncoder = passwordEncoder;
        this.compteRepository = compteRepository;
    }

    public LoginResponse authenticate(String email, String password) {

        Compte user = compteRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return new LoginResponse(false, "Email introuvable", null);
        }

        if (!passwordEncoder.matches(password, user.getMdpCrypted())) {
            return new LoginResponse(false, "Mot de passe incorrect", null);
        }

        return new LoginResponse(true, "Connexion réussie", user.getId());
    }

    public LoginResponse find(String email) {

        Compte user = compteRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return new LoginResponse(false, "Email introuvable", null);
        }

        return new LoginResponse(true, "Email trouvé", user.getId());
    }
}
