package com.uccu.textricator;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

final class StatementView {

    private final boolean verbose;

    StatementView(final boolean verbose) {
        this.verbose = verbose;
    }

    void render(final Directory directory, final Map<String, UccuStatement> statements) {
        render(directory, StringUtils.EMPTY, statements);
    }

    private void render(final Directory directory, final String padding, final Map<String, UccuStatement> statements) {
        assert !Objects.isNull(directory) && !StringUtils.isNotEmpty(padding);

        System.out.println(padding + " processing directory -- " + enrichContent(directory));

        directory.getFileChildren().stream().forEach(file -> render(file, padding + "\t", statements));
        directory.getDirectoryChildren().stream().forEach(dir -> render(dir, padding + "\t", statements));
    }

    private void render(final File file, final String padding, final Map<String, UccuStatement> statements) {
        assert !Objects.isNull(file) && !StringUtils.isNotEmpty(padding);

        System.out.println(padding + " processing file -- " + enrichContent(file, statements));
    }

    private String enrichContent(final Directory directory) {
        assert !Objects.isNull(directory);

        try {
            return this.verbose ? directory.getCreated().toString() + " : " + directory.getModified() + " : " + directory.getSize() + " " + directory.getName()
                    : directory.getFullPath();
        } catch (IOException e) {
            return this.verbose ? "E " + directory.getName() : directory.getName();
        }
    }

    private String enrichContent(final File file, final Map<String, UccuStatement> statements) {
        assert !Objects.isNull(file);

        UccuStatement statement = PDFBoxParser.parse(file);
        statements.put(statement.getYear() + "::" + statement.getMonth(), statement);
        
        try {
            return this.verbose ? file.getCreated() + " : " + file.getModified() + " : " + file.getSize() + " " + file.getName() : file.getFullPath();
        } catch (IOException e) {
            return this.verbose ? e + " :: " + file.getName() : file.getName();
        }
    }
}
