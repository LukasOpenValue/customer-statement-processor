package nl.openvalue.CustomerStatementProcessor.validator.api;

import nl.openvalue.CustomerStatementProcessor.model.Statement;

import java.util.List;

public interface ValidatorService {

    void validateStatements(List<Statement> statements);
}
