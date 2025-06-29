package com.example.Cards.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Cards.entity.Persons;
import com.example.Cards.entity.scoreLimit;
import com.example.Cards.repo.PersonsRepo;
import com.example.Cards.repo.scoreLimitRepo;

@RestController
@CrossOrigin
public class PersonController {

    @Autowired
    private PersonsRepo repo;

    @Autowired
    private scoreLimitRepo repo1;

    @PutMapping("/setlimit")
    public ResponseEntity<String> setLimit(@RequestParam int score) {
        Optional<scoreLimit> optionalLimit = repo1.findById(1);
        if (optionalLimit.isPresent()) {
            scoreLimit limit = optionalLimit.get();
            limit.setScoreLimit(score);
            repo1.save(limit);
            return ResponseEntity.ok("Score limit updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score limit with ID 1 not found.");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Persons>> getAll() {
        List<Persons> persons = repo.findAll();
        return ResponseEntity.ok(persons);
    }

    @PostMapping("/add")
    public ResponseEntity<String> create(@RequestBody Persons person) {
        repo.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person created successfully.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with ID " + id + " not found.");
        }
    }

    @PutMapping("/addscore")
    public ResponseEntity<String> update(@RequestParam int id, @RequestParam int score) {
        Optional<Persons> optionalPerson = repo.findById(id);

        if (optionalPerson.isPresent()) {
            Persons existingPerson = optionalPerson.get();
            existingPerson.setPoints(existingPerson.getPoints() + score);
            repo.save(existingPerson);

            Optional<scoreLimit> optionalLimit = repo1.findById(1);
            if (optionalLimit.isPresent()) {
                int limit = optionalLimit.get().getScoreLimit();
                if (existingPerson.getPoints() >= limit) {
                    repo.delete(existingPerson);
                    return ResponseEntity.ok(existingPerson.getName() + " eliminated (score limit exceeded).");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Score limit not set in DB.");
            }

            return ResponseEntity.ok("Score updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with ID " + id + " not found.");
        }
    }
}
