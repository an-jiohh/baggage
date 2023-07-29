package com.example.baggage.jiho.repository;

import com.example.baggage.domain.Mobom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MobomRepository extends JpaRepository<Mobom, Integer> {
    List<Mobom> findByADDRESSContainsAndHEADMENUContains(String address, String headMenu);
}
