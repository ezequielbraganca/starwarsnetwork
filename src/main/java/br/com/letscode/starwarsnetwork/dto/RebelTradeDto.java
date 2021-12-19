package br.com.letscode.starwarsnetwork.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class RebelTradeDto {

	private long userId;
	private List<InventaryDTO> inventaries;
}
