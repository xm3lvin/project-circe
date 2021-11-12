package com.kjt.circe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrintTask {

	private String ptid;
	
	private String pid;
	
	private String rid;
	
	private String oid;
	
	@JsonProperty("html")
	private String content;
	
	@JsonIgnore
	private Status status;
	
}
