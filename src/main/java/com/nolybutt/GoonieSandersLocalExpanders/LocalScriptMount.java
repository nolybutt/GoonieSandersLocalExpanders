package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.filesystem.WritableMount;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class LocalScriptMount implements WritableMount {
    private final Path root;

    LocalScriptMount(Path root) throws IOException {
        this.root = root.toAbsolutePath().normalize();
        Files.createDirectories(this.root);
    }

    @Override
    public boolean exists(String path) throws IOException {
        return Files.exists(resolve(path));
    }

    @Override
    public boolean isDirectory(String path) throws IOException {
        return Files.isDirectory(resolve(path));
    }

    @Override
    public void list(String path, List<String> contents) throws IOException {
        Path dir = resolve(path);
        if (!Files.isDirectory(dir)) return;

        try (var stream = Files.list(dir)) {
            stream.map(entry -> entry.getFileName().toString()).sorted().forEach(contents::add);
        }
    }

    @Override
    public long getSize(String path) throws IOException {
        Path resolved = resolve(path);
        if (Files.isDirectory(resolved)) return 0L;
        return Files.size(resolved);
    }

    @Override
    public SeekableByteChannel openForRead(String path) throws IOException {
        return Files.newByteChannel(resolve(path));
    }

    @Override
    public SeekableByteChannel openForWrite(String path) throws IOException {
        Path resolved = resolve(path);
        Path parent = resolved.getParent();
        if (parent != null) Files.createDirectories(parent);
        return Files.newByteChannel(resolved, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    @SuppressWarnings("removal")
    public SeekableByteChannel openForAppend(String path) throws IOException {
        Path resolved = resolve(path);
        Path parent = resolved.getParent();
        if (parent != null) Files.createDirectories(parent);
        return Files.newByteChannel(resolved, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    }

    @Override
    public BasicFileAttributes getAttributes(String path) throws IOException {
        return Files.readAttributes(resolve(path), BasicFileAttributes.class);
    }

    @Override
    public void makeDirectory(String path) throws IOException {
        Files.createDirectories(resolve(path));
    }

    @Override
    public void delete(String path) throws IOException {
        Path resolved = resolve(path);
        if (Files.isDirectory(resolved)) {
            try (var entries = Files.list(resolved)) {
                List<Path> children = new ArrayList<>();
                entries.forEach(children::add);
                for (Path child : children) {
                    delete(root.relativize(child).toString());
                }
            }
        }
        Files.deleteIfExists(resolved);
    }

    @Override
    public void rename(String source, String destination) throws IOException {
        Path from = resolve(source);
        Path to = resolve(destination);
        Path parent = to.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public SeekableByteChannel openFile(String path, Set<OpenOption> options) throws IOException {
        Path resolved = resolve(path);
        Path parent = resolved.getParent();
        if (parent != null) Files.createDirectories(parent);
        return Files.newByteChannel(resolved, options);
    }

    @Override
    public long getRemainingSpace() throws IOException {
        FileStore store = Files.getFileStore(root);
        return store.getUsableSpace();
    }

    @Override
    public long getCapacity() {
        try {
            FileStore store = Files.getFileStore(root);
            return store.getTotalSpace();
        } catch (IOException e) {
            return Long.MAX_VALUE;
        }
    }

    @Override
    public boolean isReadOnly(String path) throws IOException {
        return false;
    }

    private Path resolve(String path) throws IOException {
        Path resolved = root.resolve(path.replace("/", File.separator)).normalize();
        if (!resolved.startsWith(root)) {
            throw new IOException("Invalid path outside mount");
        }
        return resolved;
    }
}
