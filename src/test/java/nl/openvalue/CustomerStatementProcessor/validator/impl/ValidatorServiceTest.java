package nl.openvalue.CustomerStatementProcessor.validator.impl;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;

public class ValidatorServiceTest {

    private static final String RESOURCES_FOLDER = "./src/test/resources/";

    @AfterEach
    void deleteTestFiles() throws IOException {
        FileUtils.deleteDirectory(new File(RESOURCES_FOLDER + "error"));
        FileUtils.deleteDirectory(new File(RESOURCES_FOLDER + "processed"));
    }
}
