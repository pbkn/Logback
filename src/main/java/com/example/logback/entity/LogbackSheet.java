package com.example.logback.entity;

import com.univocity.parsers.annotations.Parsed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Data
public class LogbackSheet {

	@Parsed(field = "S.No.")
	private Integer no;

	@Parsed(field = "Timestamp")
	private String timestamp;

	@Parsed(field = "Called Service")
	private String className;

}
