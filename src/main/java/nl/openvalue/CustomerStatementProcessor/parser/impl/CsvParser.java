package nl.openvalue.CustomerStatementProcessor.parser.impl;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.parser.api.FileParser;
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
    Logger logger = LoggerFactory.getLogger(CsvParser.class);

    @Override
    public List<Statement> parseFile(File file) {
        List<Statement> statements = new ArrayList<>();
        try (CSVParser csvParser = CSVFormat.RFC4180.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .get()
                .parse(new FileReader(file))) {
            for (CSVRecord record : csvParser) {
                try {
                    Statement statement = mapCsvRecord(record);
                    logger.debug("Processed statement: {}", statement.reference());
                    statements.add(statement);
                } catch (IllegalArgumentException e) {
                    logger.warn("Couldn't map csv record {}", record.toString());
                }
            }
        } catch (Exception e) {
            logger.error("Error processing CSV file", e);
        }
        return statements;
    }

    private Statement mapCsvRecord(CSVRecord record) {
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
