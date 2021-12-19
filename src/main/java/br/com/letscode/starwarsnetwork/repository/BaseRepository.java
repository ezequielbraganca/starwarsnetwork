package br.com.letscode.starwarsnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.letscode.starwarsnetwork.entity.Base;

public interface BaseRepository extends JpaRepository<Base, Long> {

}
