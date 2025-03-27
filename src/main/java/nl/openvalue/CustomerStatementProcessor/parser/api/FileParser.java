package nl.openvalue.CustomerStatementProcessor.parser.api;

import nl.openvalue.CustomerStatementProcessor.model.Statement;

import java.io.File;
import java.util.List;

public interface FileParser {

    List<Statement> parseFile(File file);
}
