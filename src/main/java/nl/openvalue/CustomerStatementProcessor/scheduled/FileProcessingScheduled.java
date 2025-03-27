package nl.openvalue.CustomerStatementProcessor.scheduled;

import nl.openvalue.CustomerStatementProcessor.processor.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileProcessingScheduled {
    private final Logger logger = LoggerFactory.getLogger(FileProcessingScheduled.class);

    private final FileProcessor fileProcessor;

    @Autowired
    public FileProcessingScheduled(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void triggerFileProcessing() {
        logger.info("Triggering new file processing via the scheduled task.");
        fileProcessor.processFiles();
    }
}
