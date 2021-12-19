package br.com.letscode.starwarsnetwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.letscode.starwarsnetwork.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Optional<Item> findByName(String name);
}
