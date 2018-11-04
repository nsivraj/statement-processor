package com.uccu.textricator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

public abstract class FSNode {
    
    private final Path path;
    
    protected FSNode(final Path path) {
        Objects.requireNonNull(path);
        
        this.path = path;
    }

    protected final Path getPath() {
        return this.path;
    }

    protected final String getName() {
        return this.path.toFile().getName();
    }

    public final String getFullPath() {
    	return this.path.toAbsolutePath().toString();
    }
    
    protected final long getSize() throws IOException {
        return Files.readAttributes(this.path, BasicFileAttributes.class).size();
    }
    
    protected final FileTime getCreated() throws IOException {
        return Files.readAttributes(this.path, BasicFileAttributes.class).creationTime();
    }
    
    protected final FileTime getModified() throws IOException {
        return Files.readAttributes(this.path, BasicFileAttributes.class).lastModifiedTime();
    }
}
