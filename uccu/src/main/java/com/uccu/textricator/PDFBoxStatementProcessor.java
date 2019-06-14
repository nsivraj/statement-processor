package com.uccu.textricator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class PDFBoxStatementProcessor {

    private int year;
    private String statementsFolder;
    private String filter;
    /**
     * Map keyed with lower case full month name
     * to List of rows of data in the CSV
     */
    private final Map<String, UccuStatement> statements;
    private final StatementView view;

    public PDFBoxStatementProcessor() {
        statements = new HashMap<String, UccuStatement>();
        view = new StatementView(false);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setStatementsFolder(String statementsFolder) {
        this.statementsFolder = statementsFolder;
    }

    public void setFilter(String filter) {
		this.filter = filter;
	}

    public void printStatements() {
        // this method prints out the CSV spreadsheet for each month
    	System.out.println("Files containing statements for year: " + this.year);
    	for(UccuStatement statement : statements.values()) {
    		System.out.println(statement.getFullPath());
    		statement.writeCSV();
    	}
    }

    public void parseStatements() {
    	Optional<String> rootFolder = StringUtils.isEmpty(this.statementsFolder) ? Optional.empty() : Optional.of(this.statementsFolder);
    	
        final Path root = Paths.get(rootFolder.orElseThrow(RuntimeException::new));

        if (Files.isDirectory(root)) {
            final Directory rootDir = new Directory(root);

            Optional<String> fileFilter = StringUtils.isEmpty(this.filter) ? Optional.empty() : Optional.of(this.filter);
            list(rootDir, fileFilter);
            this.view.render(rootDir, statements);
        } else {
            throw new RuntimeException("The second argument MUST be a directory");
        }
    }

    private void list(final Directory directory, final Optional<String> filter) {
        assert !Objects.isNull(directory) && !Objects.isNull(filter);

        DirectoryStream<Path> stream = null;
        try {
            if (filter.isPresent()) {
                stream = Files.newDirectoryStream(directory.getPath(), createFilter(filter.get()));
            } else {
                stream = Files.newDirectoryStream(directory.getPath());
            }

            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    final Directory child = new Directory(path);
                    directory.addDirectory(child);

                    list(child, filter);
                } else {
                    directory.addFile(new File(path));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (!Objects.isNull(stream)) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("unable to close stream while listing directories", e);
                }
            }
        }
    }

    private Filter<Path> createFilter(final String pattern) {

        return new Filter<Path>() {

            @Override
            public boolean accept(final Path entry) throws IOException {
                return Files.isDirectory(entry) || entry.toFile().getName().contains(pattern);
            }
        };
    }
    
    public static void main(String[] args) {
        // args[0] is the number of the year and defaults to "2018", i.e. "2018"
        // args[1] is the folder containing the 12 statements for the year and defaults to "."
    	//         i.e. /Users/nsivraj/forge/tools/textricator-9.0.44/uccu/uccu_statements/2018
    	// args[2] is a filter for which files to include and defaults to ".csv"
    	//         i.e. ".txt"
        PDFBoxStatementProcessor processor = new PDFBoxStatementProcessor();
        processor.setYear(new Integer(args.length >= 1 ? args[0] : "2018"));
        processor.setStatementsFolder(args.length >= 2 ? args[1] : ".");
        processor.setFilter(args.length >= 3 ? args[2] : ".csv");
        processor.parseStatements();
        processor.printStatements();
    }

}

