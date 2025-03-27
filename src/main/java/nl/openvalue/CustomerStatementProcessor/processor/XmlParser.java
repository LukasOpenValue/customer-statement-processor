package nl.openvalue.CustomerStatementProcessor.processor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.openvalue.CustomerStatementProcessor.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParser {
    private final XmlMapper xmlMapper = new XmlMapper();
    Logger logger = LoggerFactory.getLogger(XmlParser.class);

    public List<Statement> parseXmlFile(File file) {
        List<Statement> statements = new ArrayList<>();
        try {
            Statement statement = xmlMapper.readValue(file, Statement.class);
            logger.debug("Processed statement: {}", statement.reference());
            statements.add(statement);
        } catch (Exception e) {
            logger.error("Error processing XML file", e);
        }
        return statements;
    }
}
