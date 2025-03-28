package nl.openvalue.customerstatementprocessor.parser.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.openvalue.customerstatementprocessor.model.Statement;
import nl.openvalue.customerstatementprocessor.parser.api.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParser implements FileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public List<Statement> parseFile(File file) {
        List<Statement> statements = new ArrayList<>();
        try {
            statements = xmlMapper.readValue(file, xmlMapper.getTypeFactory().constructCollectionType(List.class, Statement.class));
            LOGGER.debug("Processed XML file: {}", file.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Error processing XML file", e);
        }
        return statements;
    }
}
