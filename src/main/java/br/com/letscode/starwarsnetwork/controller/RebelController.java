package br.com.letscode.starwarsnetwork.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.letscode.starwarsnetwork.dto.InventaryDTO;
import br.com.letscode.starwarsnetwork.dto.TradeDto;
import br.com.letscode.starwarsnetwork.entity.Base;
import br.com.letscode.starwarsnetwork.entity.Inventory;
import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.entity.Rebel;
import br.com.letscode.starwarsnetwork.service.InventoryService;
import br.com.letscode.starwarsnetwork.service.ItemService;
import br.com.letscode.starwarsnetwork.service.RebelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("v1/rebels")
public class RebelController {


	private final RebelService rebelService;
	private final ItemService itemService;
	private final InventoryService inventaryService;

	@Autowired
	public RebelController(RebelService rebelService, ItemService itemService, InventoryService inventaryService) {
		this.rebelService = rebelService;
		this.itemService =  itemService;
		this.inventaryService = inventaryService;
	}
	
	@PostMapping()
	@RouterOperation(operation = @Operation(operationId = "save", summary = "Create new rebel", description = "Create new rebel",
    responses = { @ApiResponse(responseCode = "200", description = "Rebel created with success", content = @Content(schema = @Schema(implementation = Rebel.class))),
          @ApiResponse(responseCode = "304", description = "Invalid Operation"),
          @ApiResponse(responseCode = "500", description = "Internal error") }))
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody Rebel rebel){
		if(rebel.getId() != null) {
			return new ResponseEntity<> ("Rebel cannot be edited",HttpStatus.NOT_MODIFIED);
		}
		try {
			rebelService.create(rebel);
			return new ResponseEntity<> (rebel.toRebelDTO(),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> listAll(){
		try {
			return new ResponseEntity<> (rebelService.findAll(),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping(path = "{userId}/transfer/{baseId}")
	public ResponseEntity<?> transfer(@PathVariable("userId") long userId, @PathVariable("baseId") long baseId){
		try {
			Optional<Rebel> rebelOpt = rebelService.findById(userId);
			if(!rebelOpt.isPresent()) 
				return new ResponseEntity<> ("Rebel not found",HttpStatus.INTERNAL_SERVER_ERROR); 
			Rebel rebel = rebelOpt.get();
			if(rebel.getBase().getId().equals(baseId))
				return new ResponseEntity<> ("Rebel cannot be transferred to the same base",HttpStatus.INTERNAL_SERVER_ERROR);
			rebel.setBase(new Base(baseId));
			rebelService.update(rebel);
			return new ResponseEntity<> ("Transfer Rebel with success",HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "{userId}/reportBetrayal")
	public ResponseEntity<?> reportBetrayal(@PathVariable("userId") long userId){
		try {
			Optional<Rebel> rebelOpt = rebelService.findById(userId);
			if(!rebelOpt.isPresent()) 
				return new ResponseEntity<> ("Rebel not found",HttpStatus.INTERNAL_SERVER_ERROR); 
			Rebel rebel = rebelOpt.get();
			rebel.reportBetrayal();
			rebelService.update(rebel);
			return new ResponseEntity<> ("Traitor report successfully performed",HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "trade")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> tradeItens(@Valid @RequestBody TradeDto trade){
		try {
			Optional<Rebel> rebelA = rebelService.findById(trade.getRebelA().getUserId());
			Optional<Rebel> rebelB = rebelService.findById(trade.getRebelB().getUserId());
			
			if(!rebelA.isPresent() || !rebelB.isPresent()) 
				return new ResponseEntity<> ("Rebel not found",HttpStatus.BAD_REQUEST);
			if(rebelA.get().isTraitor() || rebelA.get().checkIsTraitor()) {
				return new ResponseEntity<> ("Traitor detected!!!",HttpStatus.BAD_REQUEST);
			}
			
			List<InventaryDTO> itensA = trade.getRebelA().getInventaries();
			List<InventaryDTO> itensB = trade.getRebelB().getInventaries();
			
			int pointsA = sumPoints(rebelA, itensA);
			
			int pointsB = sumPoints(rebelB, itensB);
			
			if(pointsA != pointsB) {
				return new ResponseEntity<> ("The Negotiated Points are Different!!!",HttpStatus.BAD_REQUEST);
			}
			
			sendItensRebelToRebel(rebelA.get(), rebelB.get(), itensA);
			sendItensRebelToRebel(rebelB.get(), rebelA.get(), itensB);
		    
			rebelService.update(rebelA.get());
			rebelService.update(rebelB.get());
			
			
			return new ResponseEntity<> ("Successful trade",HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<> (e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendItensRebelToRebel(Rebel rebelA, Rebel rebelB, List<InventaryDTO> itensA) {
		for(InventaryDTO inventaryDTO : itensA) {
			Item item = rebelA.getInventaries().stream().filter(n -> n.getItem().getName().equals(inventaryDTO.getItem())).findFirst().get().getItem();
			Optional<Inventory> inventaryB = rebelB.getInventaries().stream().filter(n -> n.getItem().getName().equals(inventaryDTO.getItem())).findFirst();
		    if(inventaryB.isPresent()) {
		    	Inventory invB = inventaryB.get();
		    	invB.setQuantity(invB.getQuantity() + inventaryDTO.getQuantity());
		    }else {
		    	rebelB.getInventaries().add(new Inventory(rebelB, item, inventaryDTO.getQuantity()));
		    }
		    Optional<Inventory> inventaryA = rebelA.getInventaries().stream().filter(n -> n.getItem().getName().equals(inventaryDTO.getItem())).findFirst();
		    Inventory invA = inventaryA.get();
			invA.setQuantity(invA.getQuantity() - inventaryDTO.getQuantity());
		    
		}
	}

	private int sumPoints(Optional<Rebel> firstRebel, List<InventaryDTO> firstItens) throws Exception {
		int points = 0;
		for(InventaryDTO inventaryDTO : firstItens) {
			Optional<Inventory> inventory = firstRebel.get().getInventaries().stream().filter(n -> n.getItem().getName().equals(inventaryDTO.getItem())).findFirst();
			if(!inventory.isPresent()) {
				throw new Exception("Item not localized!!!");
			}
			if(inventory.get().getQuantity() < inventaryDTO.getQuantity()) {
				throw new Exception("inventory smaller than informed!!!");
			}
			points += inventory.get().getItem().getValue() * inventaryDTO.getQuantity();
		}
		return points;
	}
	
}
