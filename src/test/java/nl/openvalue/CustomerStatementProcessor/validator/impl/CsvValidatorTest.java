package nl.openvalue.CustomerStatementProcessor.validator.impl;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CsvValidatorTest {

    private static final String RESOURCES_FOLDER = "./src/test/resources/";

    @Autowired
    private CsvValidator csvValidator;

    @Test
    void validateFile_valid() {
        csvValidator.validateFile(new File(RESOURCES_FOLDER + "valid.csv"));
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(new File(RESOURCES_FOLDER + "error/" + date + "-validation-errors.log")).doesNotExist();
    }

    @Test
    void validateFile_invalid() {
        csvValidator.validateFile(new File(RESOURCES_FOLDER + "invalid.csv"));
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(new File(RESOURCES_FOLDER + "error/" + date + "-validation-errors.log")).exists();
    }

    @AfterEach
    void deleteTestFiles() throws IOException {
        FileUtils.deleteDirectory(new File(RESOURCES_FOLDER + "error"));
        FileUtils.deleteDirectory(new File(RESOURCES_FOLDER + "processed"));
    }
}