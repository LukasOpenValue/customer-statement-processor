package nl.openvalue.CustomerStatementProcessor.validator.impl;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ValidatorServiceTest {

    private static final String RESOURCES_FOLDER = "src/test/resources/";

    @Autowired
    private ValidatorFacade validatorFacade;

    @Test
    void validateStatements_valid() {
        validatorFacade.validateStatements(buildValidStatements());
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(FileUtils.getFile(RESOURCES_FOLDER + "error/" + date + "-validation-errors.log")).doesNotExist();
    }

    @Test
    void validateStatements_invalid() {
        validatorFacade.validateStatements(buildInvalidStatements());
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(FileUtils.getFile(RESOURCES_FOLDER + "error/" + date + "-validation-errors.log")).exists();
    }

    @AfterEach
    void deleteTestFiles() throws IOException {
        FileUtils.deleteDirectory(FileUtils.getFile(RESOURCES_FOLDER + "error"));
    }

    private List<Statement> buildValidStatements() {
        List<Statement> statements = new ArrayList<>();
        statements.add(new Statement(138932L, "NL90ABNA0585647886", "Flowers for Richard Bakker", new BigDecimal("94.9"), new BigDecimal("+14.63"), new BigDecimal("109.53")));
        statements.add(new Statement(131254L, "NL93ABNA0585619023", "Candy from Vincent de Vries", new BigDecimal("5429"), new BigDecimal("-939"), new BigDecimal("4490")));
        return statements;
    }

    private List<Statement> buildInvalidStatements() {
        List<Statement> statements = new ArrayList<>();
        statements.add(new Statement(138932L, "NL90ABNA0585647886", "Flowers for Richard Bakker", new BigDecimal("94.9"), new BigDecimal("+14.63"), new BigDecimal("16.34")));
        Statement sameRecord = new Statement(131254L, "NL93ABNA0585619023", "Candy from Vincent de Vries", new BigDecimal("5429"), new BigDecimal("-939"), new BigDecimal("6368"));
        statements.add(sameRecord);
        statements.add(sameRecord);
        return statements;
    }
}
