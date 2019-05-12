package br.com.goldenbeast.sample.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.goldenbeast.sample.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

	@Override
	public List<Person> findAll();

	public List<Person> findAllByOrderByNameDesc();

}
