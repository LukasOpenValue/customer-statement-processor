package nl.openvalue.customerstatementprocessor.parser.impl;

import nl.openvalue.customerstatementprocessor.model.Statement;
import nl.openvalue.customerstatementprocessor.parser.api.FileParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParser implements FileParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvParser.class);

	@Override
	public List<Statement> parseFile(final File file) {
		List<Statement> statements = new ArrayList<>();
		try (CSVParser csvParser = CSVFormat.RFC4180.builder()
				.setHeader()
				.setSkipHeaderRecord(true)
				.get()
				.parse(new FileReader(file))) {
			for (CSVRecord record : csvParser) {
				try {
					Statement statement = mapCsvRecord(record);
					LOGGER.debug("Processed statement: {}", statement.reference());
					statements.add(statement);
				} catch (IllegalArgumentException e) {
					LOGGER.warn("Couldn't map csv record {}", record.toString());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error processing CSV file", e);
		}
		return statements;
	}

	private Statement mapCsvRecord(final CSVRecord record) {
		return new Statement(
				Long.parseLong(record.get("Reference")),
				record.get("Account Number"),
				record.get("Description"),
				new BigDecimal(record.get("Start Balance")),
				new BigDecimal(record.get("Mutation")),
				new BigDecimal(record.get("End Balance"))
		);
	}
}
