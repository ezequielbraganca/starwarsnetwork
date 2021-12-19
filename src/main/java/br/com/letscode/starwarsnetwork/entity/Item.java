package br.com.letscode.starwarsnetwork.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Item extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "The Item name field is mandatory")
	private String name;
	
	@NotNull(message = "The Item value field is mandatory")
	private int value;
	
	@OneToMany(mappedBy = "item")
    Set<Inventory> inventaries;
	

	protected Item() {
	}
	
	
}
