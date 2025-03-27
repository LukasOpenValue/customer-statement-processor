package nl.openvalue.CustomerStatementProcessor.rest;

import nl.openvalue.CustomerStatementProcessor.processor.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileProcessingRest {
    private final Logger logger = LoggerFactory.getLogger(FileProcessingRest.class);

    private final FileProcessor fileProcessor;

    @Autowired
    public FileProcessingRest(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @PostMapping(value = "/upload/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        logger.info("Triggering new file processing via the csv REST endpoint.");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("CSV file is empty.");
        }

        try {
            fileProcessor.processCsvFile(file);
            return ResponseEntity.ok("CSV file processed successfully.");
        } catch (Exception e) {
            logger.error("Error processing CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV file.");
        }
    }

    @PostMapping(value = "/upload/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadXml(@RequestParam("file") MultipartFile file) {
        logger.info("Triggering new file processing via the xml REST endpoint.");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("XML file is empty.");
        }

        try {
            fileProcessor.processXmlFile(file);
            return ResponseEntity.ok("XML file processed successfully.");
        } catch (Exception e) {
            logger.error("Error processing XML file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing XML file.");
        }
    }
}
