package nl.openvalue.CustomerStatementProcessor.validator.impl;

import nl.openvalue.CustomerStatementProcessor.processor.XmlParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class XmlParserTest {

    private static final String RESOURCES_FOLDER = "./src/test/resources/";

    @Autowired
    private XmlParser xmlParser;

    @Test
    void validateStatements_valid() {
        xmlParser.parseXmlFile(new File(RESOURCES_FOLDER + "valid.xml"));
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(new File(RESOURCES_FOLDER + "error/" + date + "-validation-errors.log")).doesNotExist();
    }

    @Test
    void validateStatements_invalid() {
        xmlParser.parseXmlFile(new File(RESOURCES_FOLDER + "invalid.xml"));
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(new File(RESOURCES_FOLDER + "error/" + date + "-validation-errors.log")).exists();
    }
}