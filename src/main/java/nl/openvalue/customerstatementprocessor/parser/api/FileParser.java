package nl.openvalue.customerstatementprocessor.parser.api;

import nl.openvalue.customerstatementprocessor.model.Statement;

import java.io.File;
import java.util.List;

public interface FileParser {

    List<Statement> parseFile(File file);
}
