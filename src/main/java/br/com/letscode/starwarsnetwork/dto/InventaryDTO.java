package br.com.letscode.starwarsnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventaryDTO {

	private long id;
	private String item;
	private int quantity;
}
