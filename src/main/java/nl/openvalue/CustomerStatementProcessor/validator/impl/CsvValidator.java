package nl.openvalue.CustomerStatementProcessor.validator.impl;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.validator.api.ValidatorService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
public class CsvValidator implements ValidatorService {
    private final ErrorLogger errorLogger;
    private final Set<Long> transactionReferences = new HashSet<>();
    Logger logger = LoggerFactory.getLogger(CsvValidator.class);
    private String fileName;

    @Autowired
    public CsvValidator(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @Override
    public void validateFile(File file) {
        fileName = file.getName();
        try (CSVParser csvParser = CSVFormat.RFC4180.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(new FileReader(file))) {
            for (CSVRecord record : csvParser) {
                Statement statement = mapCsvRecord(record);

                validateStatement(statement);
                logger.debug("Processed statement: {}", statement.reference());
            }
        } catch (Exception e) {
            logger.error("Error processing CSV file", e);
        }
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

    private void validateStatement(Statement statement) {
        if (!transactionReferences.add(statement.reference())) {
            logger.error("Duplicate statement reference: {}", statement.reference());
            errorLogger.logError("Duplicate statement reference.", statement, fileName);
        }

        BigDecimal expectedEndBalance = statement.startBalance().add(statement.mutation());
        if (!expectedEndBalance.equals(statement.endBalance())) {
            logger.error("Invalid end balance for statement {}: expected {}, found {}",
                    statement.reference(), expectedEndBalance, statement.endBalance());
            errorLogger.logError("Invalid end balance.", statement, fileName);
        }
    }
}
