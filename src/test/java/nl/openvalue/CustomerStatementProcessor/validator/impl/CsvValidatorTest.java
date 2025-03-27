package nl.openvalue.CustomerStatementProcessor.validator.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CsvValidatorTest {

    @Autowired
    private CsvValidator csvValidator;

    @Test
    void validateFile() {
        File file = new File("./src/customer-statement-processor/validator/csv-validator.csv");
        csvValidator.validateFile(file);
    }
}