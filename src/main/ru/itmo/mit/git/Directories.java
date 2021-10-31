package ru.itmo.mit.git;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Directories {
    private final String fileSeparator = File.separator;
    private final String workingDir;
    private final String storageDir;
    private final String headFile;
    private final String indexFile;
    private final String objectsDir;
    private final String commitsDir;
    private final String branchesDir;

    public String getWorkingDir() {
        return workingDir;
    }

    public String getStorageDir() {
        return storageDir;
    }

    public String getIndexFile() {
        return indexFile;
    }

    public String getBranchesDir() {
        return branchesDir;
    }

    public String getCommitsDir() {
        return commitsDir;
    }

    public Directories(String workingDir) {
        this.workingDir = workingDir;
        storageDir = workingDir + fileSeparator + ".mygit";
        headFile = storageDir + fileSeparator + "HEAD";
        indexFile = storageDir + fileSeparator + "index";
        objectsDir = storageDir + fileSeparator + "objects";
        commitsDir = storageDir + fileSeparator + "commits";
        branchesDir = storageDir + fileSeparator + "branches";
    }

    public List<String> getFiles() {
        return Arrays.asList(indexFile, headFile);
    }

    public List<String> getDirectoriesInStorage() {
        return Arrays.asList(objectsDir, commitsDir, branchesDir);
    }

    public String getObjectFileName(String objectHash) {
        return objectsDir + fileSeparator + objectHash;
    }

    public String getCommitFileName(String commitHash) {
        return commitsDir + fileSeparator + commitHash;
    }

    public String getBranchHeadFileName(String branchName) {
        return branchesDir + fileSeparator + branchName;
    }

    public String getUserFileName(String fileName) {
        return workingDir + fileSeparator + fileName;
    }
}
