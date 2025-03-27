package nl.openvalue.CustomerStatementProcessor.validator.impl;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class ErrorLogger {
    Logger logger = LoggerFactory.getLogger(ErrorLogger.class);

    @Value("${errorDirectory}")
    private String errorDirectory;

    public void logError(String message, Statement statement, String transactionFileName) {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String errorFileName = date + "-validation-errors.log";
        File errorFile = new File(errorDirectory, errorFileName);

        try {
            Files.createDirectories(Paths.get(errorDirectory));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorFile, true))) {
                writer.write(buildErrorMessage(message, statement, transactionFileName));
                writer.newLine();
            }

            logger.info("Logged error: {}", message);
        } catch (IOException e) {
            logger.error("Failed to write error to file: {}", errorFile.getAbsolutePath(), e);
        }
    }

    private String buildErrorMessage(String message, Statement statement, String fileName) {
        return String.format(
                "ERROR: %s%n" +
                        "File: %s%n" +
                        "Transaction Reference: %d%n" +
                        "Account Number: %s%n" +
                        "Description: %s%n" +
                        "-----------------------------",
                message,
                fileName,
                statement.reference(),
                statement.accountNumber(),
                statement.description()
        );
    }
}
