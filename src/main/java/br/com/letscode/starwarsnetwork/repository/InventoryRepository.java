package br.com.letscode.starwarsnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.letscode.starwarsnetwork.entity.Inventory;
import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.entity.Rebel;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	List<Inventory> getByRebel(Rebel rebel);

	Inventory findByRebelAndItem(Rebel rebel, Item item);

}
