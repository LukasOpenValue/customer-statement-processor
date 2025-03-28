package nl.openvalue.customerstatementprocessor.util;

import nl.openvalue.customerstatementprocessor.model.Statement;
import org.apache.commons.io.FileUtils;
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
public class ErrorFileHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorFileHelper.class);

    @Value("${errorDirectory}")
    private String errorDirectory;

    public void printErrorToFile(String message, Statement statement) {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String errorFileName = date + "-validation-errors.log";
        File errorFile = FileUtils.getFile(System.getProperty("user.dir"), errorDirectory, errorFileName);

        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir"), errorDirectory));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorFile, true))) {
                writer.write(buildErrorMessage(message, statement));
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to write error to file: {}", errorFile.getAbsolutePath(), e);
        }
    }

    private String buildErrorMessage(String message, Statement statement) {
        return String.format(
                "ERROR: %s%n" +
                        "Transaction Reference: %d%n" +
                        "Description: %s%n" +
                        "-----------------------------",
                message,
                statement.reference(),
                statement.description()
        );
    }
}
