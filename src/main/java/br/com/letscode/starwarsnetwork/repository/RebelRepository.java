package br.com.letscode.starwarsnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.letscode.starwarsnetwork.entity.Rebel;

public interface RebelRepository extends JpaRepository<Rebel, Long> {

	List<Rebel> findByTraitor(boolean traitor);

}
