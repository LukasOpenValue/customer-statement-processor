package nl.openvalue.CustomerStatementProcessor.parser.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.parser.api.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParser implements FileParser {
    private final XmlMapper xmlMapper = new XmlMapper();
    Logger logger = LoggerFactory.getLogger(XmlParser.class);

    @Override
    public List<Statement> parseFile(File file) {
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
