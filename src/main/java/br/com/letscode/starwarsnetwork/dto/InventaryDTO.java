package br.com.letscode.starwarsnetwork.dto;

import lombok.Getter;

@Getter
public class InventaryDTO {

	private long id;
	private String item;
	private int quantity;
	
	public InventaryDTO() {
		super();
	}
	
	public InventaryDTO(long id, String item, int quantity) {
		this.id = id;
		this.item = item;
		this.quantity = quantity;
	}

	public InventaryDTO(String item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}
    
	
}
