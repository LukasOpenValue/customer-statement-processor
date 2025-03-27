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
            statements = xmlMapper.readValue(file, xmlMapper.getTypeFactory().constructCollectionType(List.class, Statement.class));
            logger.debug("Processed XML file: {}", file.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Error processing XML file", e);
        }
        return statements;
    }
}
