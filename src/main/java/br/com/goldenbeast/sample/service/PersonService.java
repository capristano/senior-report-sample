package br.com.goldenbeast.sample.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.goldenbeast.sample.model.Person;
import br.com.goldenbeast.sample.repository.PersonRepository;

@Service
@Transactional
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	public Person findById(long id) {
		Person person = null;
		try {
			person = personRepository.findById(id).get();
		} catch (NoSuchElementException e) {}
		return person;
	}

	public List<Person> findAll() {
		return personRepository.findAll();
	}

	public List<Person> findAllByOrderByNameDesc() {
		return personRepository.findAllByOrderByNameDesc();
	}

	public Person save(Person p) {
		return personRepository.save(p);
	}

}
