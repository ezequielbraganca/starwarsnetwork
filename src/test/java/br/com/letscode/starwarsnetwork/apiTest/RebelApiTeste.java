package br.com.letscode.starwarsnetwork.apiTest;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.letscode.starwarsnetwork.dto.InventaryDTO;
import br.com.letscode.starwarsnetwork.dto.RebelDTO;
import br.com.letscode.starwarsnetwork.dto.RebelTradeDto;
import br.com.letscode.starwarsnetwork.dto.TradeDto;
import br.com.letscode.starwarsnetwork.entity.Base;
import br.com.letscode.starwarsnetwork.entity.Inventory;
import br.com.letscode.starwarsnetwork.entity.Item;
import br.com.letscode.starwarsnetwork.entity.Rebel;
import br.com.letscode.starwarsnetwork.type.Gender;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RebelApiTeste {
	
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

	
	@Test
	public void saveRebelwithSuccessTest() {
		Rebel rebel = new Rebel();
		rebel.setAge(20);
		rebel.setName("Ezequiel");
		rebel.setGender(Gender.MALE);
		rebel.setBase(new Base(1));
		List<Inventory> inventaries = new ArrayList<Inventory>();
		inventaries.add(new Inventory(null, new Item("weapon"), 2));
		rebel.setInventaries(inventaries);
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/rebels", rebel, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void saveRebelMinorAgeTest() {
		Rebel rebel = new Rebel();
		rebel.setAge(15);
		rebel.setName("Ezequiel");
		rebel.setGender(Gender.MALE);
		rebel.setBase(new Base(1));
		List<Inventory> inventaries = new ArrayList<Inventory>();
		inventaries.add(new Inventory(null, new Item("weapon"), 2));
		rebel.setInventaries(inventaries);
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/rebels", rebel, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void saveRebelWithoutBaseTest() {
		Rebel rebel = new Rebel();
		rebel.setAge(20);
		rebel.setName("Ezequiel");
		rebel.setGender(Gender.MALE);
		List<Inventory> inventaries = new ArrayList<Inventory>();
		inventaries.add(new Inventory(null, new Item("weapon"), 2));
		rebel.setInventaries(inventaries);
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/rebels", rebel, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void saveRebelWithoutInventoryTest() {
		Rebel rebel = new Rebel();
		rebel.setAge(20);
		rebel.setName("Ezequiel");
		rebel.setGender(Gender.MALE);
		rebel.setBase(new Base(1));
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/rebels", rebel, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(500);
	}
	

	@Test
	public void transferRebelOtherBaseSucessTest() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/rebels/1/transfer/2", HttpMethod.PUT, null, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void transferRebelSomeBaseErrorTest() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/rebels/1/transfer/2", HttpMethod.PUT, null, String.class);
		assertThat(response.getBody()).isEqualTo("Rebel cannot be transferred to the same base");
	}
	
	@Test
	public void transferRebelNoneExistentBaseErrorTest() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/rebels/1/transfer/200", HttpMethod.PUT, null, String.class);
		assertThat(response.getBody()).isEqualTo("Base not found");
	}
	
	@Test
	public void reportTraitorRebelSuccessTest() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/rebels/1/reportBetrayal", HttpMethod.PUT, null, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void reportTraitorNoneRebelTest() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/rebels/100/reportBetrayal", HttpMethod.PUT, null, String.class);
		assertThat(response.getBody()).isEqualTo("Rebel not found");
	}
	
	@Test
	public void tradeSuccessTest() {
		Rebel rebel1 = new Rebel();
		rebel1.setAge(20);
		rebel1.setName("Ezequiel");
		rebel1.setGender(Gender.MALE);
		rebel1.setBase(new Base(1));
		List<Inventory> inventaries = new ArrayList<Inventory>();
		inventaries.add(new Inventory(null, new Item("weapon"), 2));
		rebel1.setInventaries(inventaries);
		ResponseEntity<RebelDTO> response1 = restTemplate.postForEntity("/v1/rebels", rebel1, RebelDTO.class);

		
		Rebel rebel2 = new Rebel();
		rebel2.setAge(20);
		rebel2.setName("Ezequiel");
		rebel2.setGender(Gender.MALE);
		rebel2.setBase(new Base(1));
		List<Inventory> inventaries2 = new ArrayList<Inventory>();
		inventaries2.add(new Inventory(null, new Item("water"), 4));
		rebel1.setInventaries(inventaries2);
		ResponseEntity<RebelDTO> response2 = restTemplate.postForEntity("/v1/rebels", rebel1, RebelDTO.class);
		
		RebelTradeDto rebelA = new RebelTradeDto();
		rebelA.setUserId(response1.getBody().getId());
		List<InventaryDTO> inventariesA = new ArrayList<InventaryDTO>();
		inventariesA.add(new InventaryDTO("weapon", 1));
		rebelA.setInventaries(inventariesA);
		
		RebelTradeDto rebelB = new RebelTradeDto();
		rebelB.setUserId(response2.getBody().getId());
		List<InventaryDTO> inventariesB = new ArrayList<InventaryDTO>();
		inventariesB.add(new InventaryDTO("water", 2));
		rebelB.setInventaries(inventariesB);
		
		TradeDto trade = new TradeDto();
		trade.setRebelA(rebelA);
		trade.setRebelB(rebelB);
		
		ResponseEntity<String> responseTest = restTemplate.exchange("/v1/rebels/trade", HttpMethod.PUT, new HttpEntity<>(trade), String.class);
		assertThat(responseTest.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void tradeDiffentePointsErrorTest() {
		Rebel rebel1 = new Rebel();
		rebel1.setAge(20);
		rebel1.setName("UserA");
		rebel1.setGender(Gender.MALE);
		rebel1.setBase(new Base(1));
		List<Inventory> inventaries = new ArrayList<Inventory>();
		inventaries.add(new Inventory(null, new Item("weapon"), 2));
		rebel1.setInventaries(inventaries);
		ResponseEntity<RebelDTO> response1 = restTemplate.postForEntity("/v1/rebels", rebel1, RebelDTO.class);

		
		Rebel rebel2 = new Rebel();
		rebel2.setAge(20);
		rebel2.setName("UserB");
		rebel2.setGender(Gender.MALE);
		rebel2.setBase(new Base(1));
		List<Inventory> inventaries2 = new ArrayList<Inventory>();
		inventaries2.add(new Inventory(null, new Item("water"), 4));
		rebel1.setInventaries(inventaries2);
		ResponseEntity<RebelDTO> response2 = restTemplate.postForEntity("/v1/rebels", rebel1, RebelDTO.class);
		
		RebelTradeDto rebelA = new RebelTradeDto();
		rebelA.setUserId(response1.getBody().getId());
		List<InventaryDTO> inventariesA = new ArrayList<InventaryDTO>();
		inventariesA.add(new InventaryDTO("weapon", 1));
		rebelA.setInventaries(inventariesA);
		
		RebelTradeDto rebelB = new RebelTradeDto();
		rebelB.setUserId(response2.getBody().getId());
		List<InventaryDTO> inventariesB = new ArrayList<InventaryDTO>();
		inventariesB.add(new InventaryDTO("water", 1));
		rebelB.setInventaries(inventariesB);
		
		TradeDto trade = new TradeDto();
		trade.setRebelA(rebelA);
		trade.setRebelB(rebelB);
		
		ResponseEntity<String> responseTest = restTemplate.exchange("/v1/rebels/trade", HttpMethod.PUT, new HttpEntity<>(trade), String.class);
		assertThat(responseTest.getBody()).isEqualTo("The Negotiated Points are Different!!!");
	}
	
	@Test
	public void tradenoneItensErrorTest() {
		Rebel rebel1 = new Rebel();
		rebel1.setAge(20);
		rebel1.setName("UserA");
		rebel1.setGender(Gender.MALE);
		rebel1.setBase(new Base(1));
		List<Inventory> inventaries = new ArrayList<Inventory>();
		inventaries.add(new Inventory(null, new Item("food"), 2));
		rebel1.setInventaries(inventaries);
		ResponseEntity<RebelDTO> response1 = restTemplate.postForEntity("/v1/rebels", rebel1, RebelDTO.class);

		
		Rebel rebel2 = new Rebel();
		rebel2.setAge(20);
		rebel2.setName("UserB");
		rebel2.setGender(Gender.MALE);
		rebel2.setBase(new Base(1));
		List<Inventory> inventaries2 = new ArrayList<Inventory>();
		inventaries2.add(new Inventory(null, new Item("water"), 4));
		rebel1.setInventaries(inventaries2);
		ResponseEntity<RebelDTO> response2 = restTemplate.postForEntity("/v1/rebels", rebel1, RebelDTO.class);
		
		RebelTradeDto rebelA = new RebelTradeDto();
		rebelA.setUserId(response1.getBody().getId());
		List<InventaryDTO> inventariesA = new ArrayList<InventaryDTO>();
		inventariesA.add(new InventaryDTO("weapon", 1));
		rebelA.setInventaries(inventariesA);
		
		RebelTradeDto rebelB = new RebelTradeDto();
		rebelB.setUserId(response2.getBody().getId());
		List<InventaryDTO> inventariesB = new ArrayList<InventaryDTO>();
		inventariesB.add(new InventaryDTO("water", 1));
		rebelB.setInventaries(inventariesB);
		
		TradeDto trade = new TradeDto();
		trade.setRebelA(rebelA);
		trade.setRebelB(rebelB);
		
		ResponseEntity<String> responseTest = restTemplate.exchange("/v1/rebels/trade", HttpMethod.PUT, new HttpEntity<>(trade), String.class);
		assertThat(responseTest.getBody()).isEqualTo("Item not localized!!!");
	}
}
