package nl.openvalue.CustomerStatementProcessor.processor;

import nl.openvalue.CustomerStatementProcessor.model.Statement;
import nl.openvalue.CustomerStatementProcessor.parser.api.FileParser;
import nl.openvalue.CustomerStatementProcessor.parser.impl.CsvParser;
import nl.openvalue.CustomerStatementProcessor.parser.impl.XmlParser;
import nl.openvalue.CustomerStatementProcessor.validator.api.ValidatorService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public void processCsvFile(MultipartFile file) throws IOException {
        processFile(file, csvParser);
    }

    public void processXmlFile(MultipartFile file) throws IOException {
        processFile(file, xmlParser);
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
                    statements.addAll(csvParser.parseFile(file));
                    FileUtils.moveFile(file, FileUtils.getFile(processedDirectory));
                } else if (file.getName().endsWith(".xml")) {
                    statements.addAll(xmlParser.parseFile(file));
                    FileUtils.moveFile(file, FileUtils.getFile(processedDirectory));
                } else {
                    logger.warn("Unsupported file format: {}", file.getName());
                    FileUtils.moveFile(file, FileUtils.getFile(errorDirectory));
                }
            } catch (Exception e) {
                logger.error("Error processing file: {}", file.getName(), e);
                try {
                    FileUtils.moveFile(file, FileUtils.getFile(errorDirectory));
                } catch (IOException ex) {
                    logger.error("Error moving file to error directory {}", file.getName(), e);
                }
            }
        }
        validatorService.validateStatements(statements);
    }

    private void processFile(MultipartFile file, FileParser fileParser) throws IOException {
        File fileToProcess = new File(inputDirectory + File.separator + file.getOriginalFilename());
        file.transferTo(fileToProcess);
        List<Statement> statements = new ArrayList<>(fileParser.parseFile(fileToProcess));
        FileUtils.moveFile(fileToProcess, FileUtils.getFile(processedDirectory));
        validatorService.validateStatements(statements);
    }
}
