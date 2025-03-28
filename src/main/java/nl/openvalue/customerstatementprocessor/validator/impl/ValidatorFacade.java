package nl.openvalue.customerstatementprocessor.validator.impl;

import nl.openvalue.customerstatementprocessor.model.Statement;
import nl.openvalue.customerstatementprocessor.util.ErrorFileHelper;
import nl.openvalue.customerstatementprocessor.validator.api.ValidatorService;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorFacade.class);

    private static final Set<Long> TRANSACTION_REFERENCES = new HashSet<>();
    private final ErrorFileHelper errorFileHelper;

    @Autowired
    public ValidatorFacade(ErrorFileHelper errorFileHelper) {
        this.errorFileHelper = errorFileHelper;
    }

    @Override
    public void validateStatements(List<Statement> statements) {
        for(Statement statement : statements) {
            validateStatement(statement);
        }
    }

    private void validateStatement(Statement statement) {
        if (!TRANSACTION_REFERENCES.add(statement.reference())) {
            LOGGER.error("Duplicate statement reference: {}", statement.reference());
            errorFileHelper.printErrorToFile("Duplicate statement reference.", statement);
        }

        BigDecimal expectedEndBalance = statement.startBalance().add(statement.mutation());
        if (!expectedEndBalance.equals(statement.endBalance())) {
            LOGGER.error("Invalid end balance for statement {}: expected {}, found {}",
                    statement.reference(), expectedEndBalance, statement.endBalance());
            errorFileHelper.printErrorToFile("Invalid end balance.", statement);
        }
    }
}
