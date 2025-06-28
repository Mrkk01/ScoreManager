package com.example.Cards.repo;

import org.slf4j.helpers.Reporter;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Cards.entity.scoreLimit;

public interface scoreLimitRepo extends JpaRepository<scoreLimit, Integer> {

}
