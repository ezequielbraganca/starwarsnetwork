package br.com.letscode.starwarsnetwork.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RebelTradeDto {

	private long userId;
	private List<InventaryDTO> inventaries;
}
