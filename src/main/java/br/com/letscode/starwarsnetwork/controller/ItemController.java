package br.com.letscode.starwarsnetwork.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.service.ItemService;

@RestController
@RequestMapping("v1/itens")
public class ItemController {
	
	private final ItemService itemService;
	
	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}
	
	@PostMapping()
	public ResponseEntity<?> save(@Valid @RequestBody Item item){
		itemService.save(item);
		return item.getId() > 0 ? new ResponseEntity<> (item,HttpStatus.OK) : new ResponseEntity<> ("Error",HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping
	public ResponseEntity<?> listAll(){
		return new ResponseEntity<> (itemService.listAll(),HttpStatus.OK);
	}
	
}
