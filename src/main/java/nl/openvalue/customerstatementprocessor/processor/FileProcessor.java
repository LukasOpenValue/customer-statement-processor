package nl.openvalue.customerstatementprocessor.processor;

import nl.openvalue.customerstatementprocessor.model.Statement;
import nl.openvalue.customerstatementprocessor.parser.api.FileParser;
import nl.openvalue.customerstatementprocessor.parser.impl.CsvParser;
import nl.openvalue.customerstatementprocessor.parser.impl.XmlParser;
import nl.openvalue.customerstatementprocessor.validator.api.ValidatorService;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);
	private final CsvParser csvParser;
	private final XmlParser xmlParser;
	private final ValidatorService validatorService;
	@Value("${inputDirectory}")
	private String inputDirectory;
	@Value("${processedDirectory}")
	private String processedDirectory;
	@Value("${errorDirectory}")
	private String errorDirectory;

	@Autowired
	public FileProcessor(CsvParser csvParser, XmlParser xmlParser,
						 ValidatorService validatorService) {
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
		File inputDir =
				new File(System.getProperty("user.dir"), inputDirectory);
		if (!inputDir.exists()) {
			LOGGER.warn("Input directory does not exist: {}",
					inputDir.getAbsolutePath());
			return;
		}

		File[] files = inputDir.listFiles();
		if (files == null || files.length == 0) {
			LOGGER.info("No files to process.");
			return;
		}

		List<Statement> statements = new ArrayList<>();
		parseFiles(files, statements);
		validatorService.validateStatements(statements);
		LOGGER.info("Finished processing files.");
	}

	private void processFile(MultipartFile file, FileParser fileParser)
			throws IOException {
		File fileToProcess = FileUtils.getFile(System.getProperty("user.dir"),
				inputDirectory, file.getOriginalFilename());
		file.transferTo(fileToProcess);
		List<Statement> statements =
				new ArrayList<>(fileParser.parseFile(fileToProcess));
		moveFile(fileToProcess, processedDirectory);
		validatorService.validateStatements(statements);
		LOGGER.info("Finished processing file.");
	}

	private void parseFiles(File[] files, List<Statement> statements) {
		for (File file : files) {
			try {
				if (file.getName().endsWith(".csv")) {
					statements.addAll(csvParser.parseFile(file));
					moveFile(file, processedDirectory);
				} else if (file.getName().endsWith(".xml")) {
					statements.addAll(xmlParser.parseFile(file));
					moveFile(file, processedDirectory);
				} else {
					LOGGER.warn("Unsupported file format: {}", file.getName());
					moveFile(file, errorDirectory);
				}
			} catch (Exception e) {
				LOGGER.error("Error processing file: {}", file.getName(), e);
				try {
					moveFile(file, errorDirectory);
				} catch (IOException ex) {
					LOGGER.error("Error moving file to error directory {}",
							file.getName(), e);
				}
			}
		}
	}

	private void moveFile(File file, String targetDirectory)
			throws IOException {
		FileUtils.moveFile(file,
				FileUtils.getFile(System.getProperty("user.dir"),
						targetDirectory, file.getName()));
	}
}
