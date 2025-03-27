package nl.openvalue.CustomerStatementProcessor.validator.api;

import java.io.File;
import java.io.FileNotFoundException;

public interface ValidatorService {

    void validateFile(File file) throws FileNotFoundException;
}
