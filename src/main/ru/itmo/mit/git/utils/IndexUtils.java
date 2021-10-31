package ru.itmo.mit.git.utils;

import ru.itmo.mit.git.GitException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexUtils {
    private static String regexForSplit = " ";
    private static String addCommand = "add";

    public static void writeToIndex(String action, String fullFileName, String fileHash, String index) throws GitException {
        try (FileWriter writer = new FileWriter(index, true)) {
            writer.write(action + " " + fullFileName + " " + fileHash + System.lineSeparator());
        } catch (IOException e) {
            throw new GitException("Problem with writing to index", e);
        }
    }

    public static void getFilesFromIndex(Map<String, String> filesToAdd, Map<String, String> filesToRemove, String index) throws GitException {
        try (Stream<String> lines = Files.lines(Paths.get(index))) {
            for (String line : lines.collect(Collectors.toList())) {
                String[] data = line.split(regexForSplit);
                if (data[0].equals(addCommand)) {
                    filesToAdd.put(data[1], data[2]);
                } else {
                    filesToRemove.put(data[1], data[2]);
                }
            }
        } catch (IOException e) {
            throw new GitException("Problem with reading from index");
        }
    }
}
