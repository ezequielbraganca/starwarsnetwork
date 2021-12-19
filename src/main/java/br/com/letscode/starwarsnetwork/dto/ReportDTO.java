package br.com.letscode.starwarsnetwork.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDTO {

	private String name;
	private String extrationDate ;
	private List<ReportRowDTO> rows;
}
