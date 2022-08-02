package com.example.logback.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.logback.entity.LogbackSheet;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanProcessor;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogbackServiceImpl {

	private AtomicInteger number = new AtomicInteger();

	private static final String SHEET_LOCATION = "./data/Logback.xlsx";

	@Scheduled(cron = "0/5 * * ? * *") // Every 5 seconds
	public void runServiceCall() throws Exception {
		readingSheet();
		writingSheet();
//		throw new LogbackException("Custom Exception to check log file status");
	}

	public void writingSheet() throws Exception {

		try (Writer outputWriter = new OutputStreamWriter(new FileOutputStream(new File(SHEET_LOCATION)),
				StandardCharsets.UTF_8)) {

			BeanWriterProcessor<LogbackSheet> rowProcessor = new BeanWriterProcessor<>(LogbackSheet.class);
			LogbackSheet row = LogbackSheet.builder().no(number.incrementAndGet())
					.timestamp(LocalDateTime.now().toString()).className(LogbackServiceImpl.class.getSimpleName())
					.build();

			CsvWriterSettings settings = new CsvWriterSettings();
			settings.setHeaders("S.No.", "Timestamp", "Called Service");
			settings.setRowWriterProcessor(rowProcessor);
			CsvWriter writer = new CsvWriter(outputWriter, settings);
			writer.writeHeaders();
			writer.processRecord(row);
			log.trace("Logback Sheet created successfully");
		}

	}

	public void readingSheet() throws Exception {

		try (Reader inputReader = new InputStreamReader(new FileInputStream(new File(SHEET_LOCATION)),
				StandardCharsets.UTF_8)) {

			List<LogbackSheet> logbackSheetList = new ArrayList<>();

			BeanProcessor<LogbackSheet> rowProcessor = new BeanProcessor<>(LogbackSheet.class) {

				@Override
				public void beanProcessed(LogbackSheet logbackSheet, ParsingContext context) {
					logbackSheetList.add(logbackSheet);
				}
			};

			CsvParserSettings settings = new CsvParserSettings();
			settings.setProcessor(rowProcessor);
			CsvParser parser = new CsvParser(settings);
			parser.parseAllRecords(inputReader);

			log.trace("Logback Sheet successfully parsed from file");

			for (LogbackSheet logbackSheet : logbackSheetList) {
				log.info(logbackSheet.toString());
			}

		}
	}

}
