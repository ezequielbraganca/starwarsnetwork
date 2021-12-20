package br.com.letscode.starwarsnetwork.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Base extends AbstractEntity {

	public Base() {
	}
	
	public Base(long id) {
		this.id = id;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "The Base name field is mandatory")
	private String name;
	
	@NotNull(message = "The rebel latitude field is mandatory")
	private double latitude;
	
	@NotNull(message = "The rebel longitude field is mandatory")
	private double longitude;
	
	@JsonIgnore
	@OneToMany(mappedBy = "base")
	private Set<Rebel> rebels;
	
}
