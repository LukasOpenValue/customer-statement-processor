package nl.openvalue.customerstatementprocessor.validator.api;

import nl.openvalue.customerstatementprocessor.model.Statement;

import java.util.List;

public interface ValidatorService {

    void validateStatements(List<Statement> statements);
}
