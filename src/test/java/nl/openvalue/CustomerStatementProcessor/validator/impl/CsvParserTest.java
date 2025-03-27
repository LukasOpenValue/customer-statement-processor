package nl.openvalue.CustomerStatementProcessor.validator.impl;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.parser.impl.CsvParser;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CsvParserTest {

    private static final String RESOURCES_FOLDER = "./src/test/resources/";

    @Autowired
    private CsvParser csvParser;

    @Test
    void parseValidCsv() {
        List<Statement> statements = csvParser.parseFile(FileUtils.getFile(RESOURCES_FOLDER + "valid.csv"));
        assertThat(statements).isNotNull();
        assertThat(statements.size()).isEqualTo(3);
        assertThat(statements.getFirst())
                .extracting(Statement::reference, Statement::accountNumber, Statement::description, Statement::startBalance, Statement::mutation, Statement::endBalance)
                .containsExactly(183398L, "NL56RABO0149876948", "Clothes from Richard de Vries", new BigDecimal("33.34"), new BigDecimal("+5.55"), new BigDecimal("38.89"));
        assertThat(statements.get(1))
                .extracting(Statement::reference, Statement::accountNumber, Statement::description, Statement::startBalance, Statement::mutation, Statement::endBalance)
                .containsExactly(112806L, "NL27SNSB0917829871", "Subscription from Jan Dekker", new BigDecimal("28.95"), new BigDecimal("-19.44"), new BigDecimal("9.51"));
    }

    @Test
    void parseInvalidCsv() {
        List<Statement> statements = csvParser.parseFile(FileUtils.getFile(RESOURCES_FOLDER + "invalid.csv"));
        assertThat(statements).isNotNull();
        assertThat(statements.size()).isEqualTo(1);
        assertThat(statements.getFirst())
                .extracting(Statement::reference, Statement::accountNumber, Statement::description, Statement::startBalance, Statement::mutation, Statement::endBalance)
                .containsExactly(110784L, "NL93ABNA0585619023", "Subscription from Richard Bakker", new BigDecimal("13.89"), new BigDecimal("-46.18"), new BigDecimal("-32.29"));
    }
}