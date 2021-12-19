package br.com.letscode.starwarsnetwork.type;

import lombok.Getter;

@Getter
public enum ReportType {

	PERCENTAGE_REBELS("Percentage of Rebels"), 
	PERCENTAGE_TRAITORS("Percentage of Traitors"),
	INVENTORY("Average amount of each resource type per rebel"),
	LOST_POINTS("Points Lost due to Traitors");

	private final String name;

	private ReportType(String name) {
		this.name = name;
	}


}
