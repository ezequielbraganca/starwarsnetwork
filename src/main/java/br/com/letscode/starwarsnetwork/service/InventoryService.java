package br.com.letscode.starwarsnetwork.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.starwarsnetwork.entity.Inventory;
import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.entity.Rebel;
import br.com.letscode.starwarsnetwork.repository.InventoryRepository;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	@Autowired
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}
	
	public List<Inventory> findAll(){
		return inventoryRepository.findAll();
	}

	public List<Inventory> findByRebel(Rebel rebel) {
		return inventoryRepository.getByRebel(rebel);
	}

	public Inventory findByRebelAnditem(Rebel rebel, Item item) {
		return inventoryRepository.findByRebelAndItem(rebel,item);
	}

	
}
