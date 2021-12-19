package br.com.letscode.starwarsnetwork.entity;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.letscode.starwarsnetwork.dto.InventaryDTO;
import br.com.letscode.starwarsnetwork.dto.RebelDTO;
import br.com.letscode.starwarsnetwork.type.Gender;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Rebel extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "The rebel name field is mandatory")
	private String name;
	
	@NotNull(message = "The rebel age field is mandatory")
	@Min(18)
	private int age;
	
	@NotNull(message = "The rebel gender field is mandatory")
	@Enumerated(EnumType.ORDINAL)
	private Gender gender;
	
	@NotNull(message = "The rebel base field is mandatory")
	@ManyToOne
    @JoinColumn(name = "base_id")
	private Base base;
	
	@OneToMany(mappedBy = "rebel", cascade = CascadeType.ALL)
    List<Inventory> inventaries;
	
	private boolean traitor;
	
	private int numberBetrayalReports;
	
	public boolean checkIsTraitor() {
		if(numberBetrayalReports >=3) {
			traitor = true;
			return true;
		}else {
			return false;
		}
	}
	
	public void reportBetrayal() {
		this.numberBetrayalReports++;
		checkIsTraitor();
	}

	public RebelDTO toRebelDTO() {
		List<InventaryDTO> inventariesDto = inventaries.stream().map(n -> n.toInventoryDTO()).collect(toList());
		return new RebelDTO(this.id, this.name, this.age, base.getName(), inventariesDto);
	}
	
}
