package nl.openvalue.CustomerStatementProcessor.processor;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.validator.api.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileProcessor {
    private final CsvParser csvParser;
    private final XmlParser xmlParser;
    private final ValidatorService validatorService;
    Logger logger = LoggerFactory.getLogger(FileProcessor.class);
    @Value("${inputDirectory}")
    private String inputDirectory;
    @Value("${processedDirectory}")
    private String processedDirectory;
    @Value("${errorDirectory}")
    private String errorDirectory;

    @Autowired
    public FileProcessor(CsvParser csvParser, XmlParser xmlParser, ValidatorService validatorService) {
        this.csvParser = csvParser;
        this.xmlParser = xmlParser;
        this.validatorService = validatorService;
    }

    public void processFiles() {
        File inputDir = new File(inputDirectory);
        if (!inputDir.exists()) {
            logger.warn("Input directory does not exist: {}", inputDir.getAbsolutePath());
            return;
        }

        File[] files = inputDir.listFiles();
        if (files == null || files.length == 0) {
            logger.info("No files to process.");
            return;
        }


        List<Statement> statements = new ArrayList<>();
        for (File file : files) {
            try {
                if (file.getName().endsWith(".csv")) {
                    statements.addAll(csvParser.parseCsvFile(file));
                    moveFile(file, processedDirectory);
                } else if (file.getName().endsWith(".xml")) {
                    statements.addAll(xmlParser.parseXmlFile(file));
                    moveFile(file, processedDirectory);
                } else {
                    logger.warn("Unsupported file format: {}", file.getName());
                    moveFile(file, errorDirectory);
                }
            } catch (Exception e) {
                logger.error("Error processing file: {}", file.getName(), e);
                moveFile(file, errorDirectory);
            }
        }
        validatorService.validateStatements(statements);
    }

    private void moveFile(File file, String targetDir) {
        File dest = new File(targetDir, file.getName());
        file.renameTo(dest);
    }
}
