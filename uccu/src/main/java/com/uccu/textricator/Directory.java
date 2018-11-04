package com.uccu.textricator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class Directory extends FSNode {

    private List<Directory> directoryChildren;
    private List<File> fileChildren;

    Directory(final Path path) {
        super(path);

        this.directoryChildren = new ArrayList<>();
        this.fileChildren = new ArrayList<>();
    }

    void addDirectory(final Directory child) {
        assert !Objects.isNull(child);

        this.directoryChildren.add(child);
    }

    void addFile(final File child) {
        assert !Objects.isNull(child);

        this.fileChildren.add(child);
    }

    List<Directory> getDirectoryChildren() {
        return this.directoryChildren;
    }

    List<File> getFileChildren() {
        return this.fileChildren;
    }
}
