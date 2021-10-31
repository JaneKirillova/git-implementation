package ru.itmo.mit.git.utils;

import ru.itmo.mit.git.GitException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DirectoriesUtils {
    public static List<String> getAllFilesInDirectory(String directory, Predicate<? super Path> predicate) throws GitException {
        try {
            return Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(predicate)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GitException("Problem with getting all files in directory", e);
        }
    }

    public static void deleteAllFilesInDirectory(String directory, Predicate<? super String> predicate) throws GitException {
        try {
            Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(predicate)
                    .map(File::new)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new GitException("Problem with deleting files", e);
        }
    }

    public static void deleteEmptyDirsFromDirectory(String directory) throws GitException {
        try {
            Files.walk(Paths.get(directory))
                    .filter(Files::isDirectory)
                    .map(Path::toString)
                    .map(File::new)
                    .filter(file -> file.list().length == 0)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new GitException("Problem with deleting empty directories", e);
        }
    }
}
