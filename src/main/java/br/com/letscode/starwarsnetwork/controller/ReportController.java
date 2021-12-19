package br.com.letscode.starwarsnetwork.controller;

import static br.com.letscode.starwarsnetwork.utils.DateUtils.dateToString;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.letscode.starwarsnetwork.dto.ReportDTO;
import br.com.letscode.starwarsnetwork.dto.ReportRowDTO;
import br.com.letscode.starwarsnetwork.entity.Inventory;
import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.entity.Rebel;
import br.com.letscode.starwarsnetwork.service.InventoryService;
import br.com.letscode.starwarsnetwork.service.ItemService;
import br.com.letscode.starwarsnetwork.service.RebelService;
import br.com.letscode.starwarsnetwork.type.ReportType;

@RestController
@RequestMapping("v1/reports")
public class ReportController {

	private final RebelService rebelService;
	private final ItemService itemService;
	private final InventoryService inventoryService;

	@Autowired
	public ReportController(RebelService rebelService, ItemService itemService, InventoryService inventoryService) {
		this.rebelService = rebelService;
		this.itemService = itemService;
		this.inventoryService = inventoryService;
	}

	@GetMapping("traitors")
	public ResponseEntity<?> traitorsReport() {
		try {
			List<Rebel> rebels = rebelService.findAll();
			List<Rebel> traitors = rebels.stream().filter(n -> n.isTraitor()).collect(toList());

			List<ReportRowDTO> rows = new ArrayList<ReportRowDTO>();
			rows.add(ReportRowDTO.builder().name("PERCENTAGE")
					.value(String.valueOf(traitors.size() * 100 / rebels.size())).build());

			return new ResponseEntity<>(buildReport(ReportType.PERCENTAGE_TRAITORS.getName(), rows), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("rebels")
	public ResponseEntity<?> rebelsReport() {
		try {
			List<Rebel> rebels = rebelService.findAll();
			if (rebels.isEmpty()) {
				return new ResponseEntity<>("Rebels not found", HttpStatus.NOT_ACCEPTABLE);
			}

			List<Rebel> traitors = rebels.stream().filter(n -> n.isTraitor()).collect(toList());

			List<ReportRowDTO> rows = new ArrayList<ReportRowDTO>();
			int percentTraitors = traitors.size() * 100 / rebels.size();
			rows.add(ReportRowDTO.builder().name("PERCENTAGE").value(String.valueOf(100 - percentTraitors)).build());

			return new ResponseEntity<>(buildReport(ReportType.PERCENTAGE_REBELS.getName(), rows), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("inventory")
	public ResponseEntity<?> inventoryRebelsReport() {
		try {
			List<Item> itens = itemService.listAll();
			List<Rebel> rebels = rebelService.findAll();
			List<Rebel> rebelsActive = rebels.stream().filter(n -> !n.isTraitor()).collect(toList());

			List<ReportRowDTO> rows = new ArrayList<ReportRowDTO>();
			itens.forEach(item -> rows.add(calculateInventary(item, rebelsActive)));

			return new ResponseEntity<>(buildReport(ReportType.INVENTORY.getName(), rows), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("lostPoints")
	public ResponseEntity<?> lostPointsReport() {
		try {
			List<Rebel> traitors = rebelService.findByTraitor();

			int points = 0;
			for (Rebel traitor : traitors) {
				points += traitor.getInventaries().stream().map(t -> t.getItem().getValue() * t.getQuantity()).reduce(0, Integer::sum);
			}

			List<ReportRowDTO> rows = new ArrayList<ReportRowDTO>();
			rows.add(ReportRowDTO.builder().name("Lost Points").value(String.valueOf(points)).build());

			return new ResponseEntity<>(buildReport(ReportType.LOST_POINTS.getName(), rows), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ReportRowDTO calculateInventary(Item item, List<Rebel> rebels) {
		int quantity = 0;
		for(Rebel rebel : rebels) {
			Inventory inventary = inventoryService.findByRebelAnditem(rebel, item);
			if(inventary != null) {
				quantity += inventary.getQuantity();
			}
		}

		return ReportRowDTO.builder().name(item.getName()).value(String.valueOf(quantity / rebels.size())).build();
	}

	private ReportDTO buildReport(String name, List<ReportRowDTO> rows) {
		return ReportDTO.builder().name(name).extrationDate(dateToString(new Date())).rows(rows).build();
	}
}
