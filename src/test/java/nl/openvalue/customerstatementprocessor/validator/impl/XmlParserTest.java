package nl.openvalue.customerstatementprocessor.validator.impl;

import nl.openvalue.customerstatementprocessor.model.Statement;
import nl.openvalue.customerstatementprocessor.parser.impl.XmlParser;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class XmlParserTest {

    private static final String RESOURCES_FOLDER = "./src/test/resources/";

    @Autowired
    private XmlParser xmlParser;

    @Test
    void parseValidXml() {
        List<Statement> statements = xmlParser.parseFile(FileUtils.getFile(RESOURCES_FOLDER + "valid.xml"));
        assertThat(statements).isNotNull();
        assertThat(statements.size()).isEqualTo(2);
        assertThat(statements.getFirst())
                .extracting(Statement::reference, Statement::accountNumber, Statement::description, Statement::startBalance, Statement::mutation, Statement::endBalance)
                .containsExactly(138932L, "NL90ABNA0585647886", "Flowers for Richard Bakker", new BigDecimal("94.9"), new BigDecimal("+14.63"), new BigDecimal("109.53"));
        assertThat(statements.getLast())
                .extracting(Statement::reference, Statement::accountNumber, Statement::description, Statement::startBalance, Statement::mutation, Statement::endBalance)
                .containsExactly(131254L, "NL93ABNA0585619023", "Candy from Vincent de Vries", new BigDecimal("5429"), new BigDecimal("-939"), new BigDecimal("6368"));
    }

    @Test
    void parseInvalidXml() {
        List<Statement> statements = xmlParser.parseFile(FileUtils.getFile(RESOURCES_FOLDER + "invalid.xml"));
        assertThat(statements).isNotNull();
        assertThat(statements.size()).isEqualTo(0);
    }

    @Test
    void parseEmptyXml() {
        List<Statement> statements = xmlParser.parseFile(FileUtils.getFile(RESOURCES_FOLDER + "empty.xml"));
        assertThat(statements).isNotNull();
        assertThat(statements.size()).isEqualTo(0);
    }
}