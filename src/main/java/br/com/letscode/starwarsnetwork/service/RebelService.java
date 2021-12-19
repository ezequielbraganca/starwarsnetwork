package br.com.letscode.starwarsnetwork.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.starwarsnetwork.entity.Base;
import br.com.letscode.starwarsnetwork.entity.Inventory;
import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.entity.Rebel;
import br.com.letscode.starwarsnetwork.repository.BaseRepository;
import br.com.letscode.starwarsnetwork.repository.ItemRepository;
import br.com.letscode.starwarsnetwork.repository.RebelRepository;

@Service
public class RebelService {

	private final RebelRepository rebelRepository;
	
	private final BaseRepository baseRepository;
	
	private final ItemRepository ItemRepository;

	@Autowired
	public RebelService(RebelRepository rebelRepository, BaseRepository baseRepository, ItemRepository itemRepository) {
		this.rebelRepository = rebelRepository;
		this.baseRepository = baseRepository;
		this.ItemRepository = itemRepository;
	}
	
	public void create( Rebel rebel) throws Exception {
		Optional<Base> base = baseRepository.findById(rebel.getBase().getId());
		if(!base.isPresent()) 
			throw new Exception("Base not found");
		for(Inventory inventary: rebel.getInventaries()) {
			Optional<Item> item = ItemRepository.findByName(inventary.getItem().getName());
			if(!item.isPresent())
				throw new Exception("Item not found: " + inventary.getItem().getName());
			inventary.setItem(item.get());
			inventary.setRebel(rebel);
		}
		rebelRepository.save(rebel);
	}

	public Optional<Rebel> findById(long rebelId) {
		return rebelRepository.findById(rebelId);
	}

	public void update(Rebel rebel) throws Exception {
		Optional<Base> base = baseRepository.findById(rebel.getBase().getId());
		if(!base.isPresent()) 
			throw new Exception("Base not found");
		rebelRepository.save(rebel);
	}

	public List<Rebel> findAll() {
		return rebelRepository.findAll();
	}

	public List<Rebel> findByTraitor() {
		return rebelRepository.findByTraitor(true);
	}
}
