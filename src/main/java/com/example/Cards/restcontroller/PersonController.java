package com.example.Cards.restcontroller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Cards.entity.Persons;
import com.example.Cards.entity.scoreLimit;
import com.example.Cards.repo.PersonsRepo;
import com.example.Cards.repo.scoreLimitRepo;
import com.fasterxml.jackson.core.util.JsonRecyclerPools.LockFreePool;

@RestController
@CrossOrigin
public class PersonController {
	@Autowired
	PersonsRepo repo;
	@Autowired
	scoreLimitRepo repo1;
	@PutMapping("/setlimit")
	public ResponseEntity<Void> setLimit(@RequestParam int score){
		scoreLimit scoreLimit = repo1.getById(1);
		scoreLimit.setScoreLimit(score);
		repo1.save(scoreLimit);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<Persons>> getAll(){
		List<Persons> persons =repo.findAll();
		return new ResponseEntity<List<Persons>>(persons,HttpStatus.OK);
	}
	@PostMapping("/add")
	public ResponseEntity<String> create(@RequestBody Persons person){
		repo.save(person);
		return new ResponseEntity<String>("Created Succesfully",HttpStatus.CREATED);
	}
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam int id){
		repo.deleteById(id);
		return new ResponseEntity<String>("Deleted Succesfully",HttpStatus.OK);
		
	}
	
	@PutMapping("/addscore")
	public ResponseEntity<String> update(@RequestParam int id,@RequestParam int score){
		Persons existingperson = repo.findById(id).get();
		existingperson.setPoints(existingperson.getPoints()+score);
		repo.save(existingperson);
		scoreLimit scoreLimit = repo1.getById(1);
		
		if(existingperson.getPoints() >= scoreLimit.getScoreLimit()) {
			repo.delete(existingperson);
			return new ResponseEntity<String>(existingperson.getName() + " Eliminated",HttpStatus.OK);
		}
		
		
		
		return new ResponseEntity<String>("Updated Succesfully",HttpStatus.OK);

	}
	
	
}
