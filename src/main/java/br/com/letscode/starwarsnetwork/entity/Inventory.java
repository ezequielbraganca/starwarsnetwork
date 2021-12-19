package br.com.letscode.starwarsnetwork.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.letscode.starwarsnetwork.dto.InventaryDTO;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventory extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Inventory() {

	}

	public Inventory(Rebel rebel, Item item, int quantity) {
		this.rebel = rebel;
		this.item = item;
		this.quantity = quantity;
	}

	@ManyToOne
	@JoinColumn(name = "rebel_id")
	private Rebel rebel;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	private int quantity;

	public InventaryDTO toInventoryDTO() {
		return new InventaryDTO(this.item.id, this.item.getName(), this.quantity);
	}

}
