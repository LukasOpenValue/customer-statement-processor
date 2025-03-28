package nl.openvalue.customerstatementprocessor.rest;

import nl.openvalue.customerstatementprocessor.processor.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingRest.class);

    private final FileProcessor fileProcessor;

    public FileProcessingRest(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @PostMapping(value = "/upload/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Triggering new file processing via the csv REST endpoint.");
        ResponseEntity<String> body = checkFile(file, "csv");
        if (body != null) return body;

        try {
            fileProcessor.processCsvFile(file);
            return ResponseEntity.ok("CSV file processed successfully.");
        } catch (Exception e) {
            LOGGER.error("Error processing CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV file.");
        }
    }

    @PostMapping(value = "/upload/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadXml(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Triggering new file processing via the xml REST endpoint.");
        ResponseEntity<String> body = checkFile(file, "xml");
        if (body != null) return body;

        try {
            fileProcessor.processXmlFile(file);
            return ResponseEntity.ok("XML file processed successfully.");
        } catch (Exception e) {
            LOGGER.error("Error processing XML file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing XML file.");
        }
    }

    private ResponseEntity<String> checkFile(MultipartFile file, String fileEnding) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(fileEnding + " file is empty.");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Provided file is not a ." + fileEnding + " file.");
        }
        return null;
    }
}
