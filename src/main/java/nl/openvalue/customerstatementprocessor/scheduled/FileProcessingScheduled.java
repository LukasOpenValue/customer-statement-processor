package nl.openvalue.customerstatementprocessor.scheduled;

import nl.openvalue.customerstatementprocessor.processor.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
@Component
public class FileProcessingScheduled {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingScheduled.class);

    private final FileProcessor fileProcessor;

    @Autowired
    public FileProcessingScheduled(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void triggerFileProcessing() {
        LOGGER.info("Triggering new file processing via the scheduled task.");
        fileProcessor.processFiles();
    }
}
