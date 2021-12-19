package br.com.letscode.starwarsnetwork.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RebelDTO {

	private long id;
	private String name;
	private int age;
	private String baseName;
	private List<InventaryDTO> inventaries;
}
