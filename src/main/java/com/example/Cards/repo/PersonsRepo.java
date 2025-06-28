package com.example.Cards.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Cards.entity.Persons;

public interface PersonsRepo extends JpaRepository<Persons, Integer>{
	
}
