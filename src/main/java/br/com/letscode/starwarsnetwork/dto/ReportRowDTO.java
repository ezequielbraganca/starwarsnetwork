package br.com.letscode.starwarsnetwork.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportRowDTO {

	private String name;
	private String value;
}
