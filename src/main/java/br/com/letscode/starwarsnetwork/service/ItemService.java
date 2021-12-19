package br.com.letscode.starwarsnetwork.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.repository.ItemRepository;

@Service
public class ItemService {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
	public void save(Item item) {
		itemRepository.save(item);
	}
	
	public List<Item> listAll(){
		return itemRepository.findAll();
	}
	
	
}
