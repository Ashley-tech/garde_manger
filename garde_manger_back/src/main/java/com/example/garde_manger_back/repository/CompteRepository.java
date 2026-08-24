package com.example.garde_manger_back.repository;

import com.example.garde_manger_back.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Integer> {

    Optional<Compte> findByEmail(String email);

}