package nl.openvalue.CustomerStatementProcessor.validator.impl;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.util.ErrorLogger;
import nl.openvalue.CustomerStatementProcessor.validator.api.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ValidatorFacade implements ValidatorService {
    Logger logger = LoggerFactory.getLogger(ValidatorFacade.class);

    private final ErrorLogger errorLogger;
    private final Set<Long> transactionReferences = new HashSet<>();
    private String fileName;

    @Autowired
    public ValidatorFacade(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @Override
    public void validateStatements(List<Statement> statements) {
        for(Statement statement : statements) {
            validateStatement(statement);
        }
    }

    private void validateStatement(Statement statement) {
        if (!transactionReferences.add(statement.reference())) {
            logger.error("Duplicate statement reference: {}", statement.reference());
            errorLogger.logError("Duplicate statement reference.", statement, fileName);
        }

        BigDecimal expectedEndBalance = statement.startBalance().add(statement.mutation());
        if (!expectedEndBalance.equals(statement.endBalance())) {
            logger.error("Invalid end balance for statement {}: expected {}, found {}",
                    statement.reference(), expectedEndBalance, statement.endBalance());
            errorLogger.logError("Invalid end balance.", statement, fileName);
        }
    }
}
