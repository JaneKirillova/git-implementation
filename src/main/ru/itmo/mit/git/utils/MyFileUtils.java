package ru.itmo.mit.git.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import ru.itmo.mit.git.Directories;
import ru.itmo.mit.git.GitException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyFileUtils {
    public static void createStorage(Directories directories) throws GitException {
        File gitStorage = new File(directories.getStorageDir());
        if (!gitStorage.exists()) {
            if (!gitStorage.mkdir()) {
                throw new GitException("Problem with creating git storage");
            }
        }
        for(var file: directories.getFiles()) {
            File newFile = new File(file);
            if (!newFile.exists()) {
                try {
                    if (!newFile.createNewFile()) {
                        throw new GitException("Problem with creating git storage");
                    }
                } catch (IOException e) {
                    throw new GitException("Problem with creating git storage", e);
                }
            }
        }
        for(var directory: directories.getDirectoriesInStorage()) {
            File newDirectory = new File(directory);
            if (!newDirectory.exists()) {
                if (!newDirectory.mkdir()) {
                    throw new GitException("Problem with creating git storage");
                }
            }
        }
    }

    public static void createFile(String fileName) throws GitException {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new GitException("Problem with creating file");
                }
            } catch (IOException e) {
                throw new GitException("Problem with creating file", e);
            }
        }
    }

    public static File copyFile(String srcFileName, String dstFileName) throws GitException {
        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);
        if (!dstFile.exists()) {
            try {
                if (!dstFile.createNewFile()) {
                    throw new GitException("Problem with creating file");
                }
            } catch (IOException e) {
                throw new GitException("Problem with creating file", e);
            }
        }
        try {
            FileUtils.copyFile(srcFile, dstFile);
        } catch (IOException e) {
            throw new GitException("Problem with coping file", e);
        }
        return srcFile;
    }

    public static void clearFile(String fileName) throws GitException {
        File file = new File(fileName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new GitException("Problem with clearing file");
            }
        }
        try {
            if (!file.createNewFile()) {
                throw new GitException("Problem with clearing file");
            }
        } catch (IOException e) {
            throw new GitException("Problem with clearing file", e);
        }
    }

    public static Boolean isFilesContentsDifferent(String fileOneName, String fileTwoName) throws GitException {
        File fileOne = new File(fileOneName);
        File fileTwo = new File(fileTwoName);
        try {
            return !FileUtils.contentEquals(fileOne, fileTwo);
        } catch (IOException e) {
            throw new GitException("Problem with comparing files content", e);
        }
    }

    public static String getFileHash(String fileName) throws GitException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(fileName))) {
            return DigestUtils.sha1Hex(fileName + inputStream);
        } catch (Exception e) {
            throw new GitException("Problem with getting file hash", e);
        }
    }

    public static void deleteLinesFromFile(String fileName, Predicate<? super String> predicateToDelete) throws GitException {
        List<String> fileContent;
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            fileContent = lines
                    .filter(predicateToDelete)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GitException("Problem with deleting lines from file", e);
        }
        clearFile(fileName);
        try (Writer writer = new FileWriter(fileName)){
            for (var line: fileContent) {
                writer.write(line);
            }
        } catch (IOException e) {
            throw new GitException("Problem with deleting lines from file", e);
        }
    }

    public static Boolean checkIfFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static void deleteFile(String fileName) throws GitException {
        File file = new File(fileName);
        if (!file.delete()) {
            throw new GitException("Problem with deleting file");
        }
    }
}
