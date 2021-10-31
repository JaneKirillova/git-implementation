package ru.itmo.mit.git;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import ru.itmo.mit.git.objects.*;
import ru.itmo.mit.git.utils.*;


public class MyGit {
    private Head head;
    private Commit headCommit;
    private final Directories directories;
    private String currentBranchName = GitConstants.MASTER;

    public MyGit(String workingDir) {
        directories = new Directories(workingDir);
    }

    public String getWorkingDir() {
        return directories.getWorkingDir();
    }

    private void updateHead(Commit commit, String branchName, Boolean needToWriteToFile) throws GitException {
        headCommit = commit;
        head = new Head(headCommit, branchName);
        if (needToWriteToFile) {
            SerializationUtils.writeHeadToFile(head, directories.getBranchHeadFileName(head.getBranchName()));
        }
    }

    public void init() throws GitException {
        MyFileUtils.createStorage(directories);
        updateHead(new Commit("Initial commit", System.getProperty("user.name"), new Date(),  null), currentBranchName, true);
        SerializationUtils.writeCommitToFile(headCommit, directories.getCommitsDir());
    }

    public void add(List<String> filesToAdd) throws GitException {
        MyFileUtils.createFile(directories.getIndexFile());
        for (var fileToAddName: filesToAdd) {
            if (!MyFileUtils.checkIfFileExists(directories.getUserFileName(fileToAddName))) {
                throw new GitIncorrectInputException("User file does not exists");
            }
            String fileHash = MyFileUtils.getFileHash(directories.getUserFileName(fileToAddName));
            File fileToAdd = MyFileUtils.copyFile(directories.getUserFileName(fileToAddName),
                    directories.getObjectFileName(fileHash));
            IndexUtils.writeToIndex("add", fileToAdd.getAbsolutePath(), fileHash, directories.getIndexFile());
        }
    }

    public void remove(List<String> filesToRemove) throws GitException {
        MyFileUtils.createFile(directories.getIndexFile());
        for (var fileToRemoveName: filesToRemove) {
            if (!MyFileUtils.checkIfFileExists(directories.getUserFileName(fileToRemoveName))) {
                throw new GitIncorrectInputException("User file does not exists");
            }
            String fileHash = MyFileUtils.getFileHash(directories.getUserFileName(fileToRemoveName));
            File fileToRemove = new File(directories.getUserFileName(fileToRemoveName));
            IndexUtils.writeToIndex("rm",
                    fileToRemove.getAbsolutePath(),
                    fileHash,
                    directories.getIndexFile());
        }
    }

    public void commit(String message) throws GitException {
        Commit commit = new Commit(message, System.getProperty("user.name"), new Date(), head.getCommit().getCommitHash());
        commit.addParentFiles(headCommit);
        Map<String, String> filesToAdd = new HashMap<>();
        Map<String, String> filesToRemove = new HashMap<>();
        IndexUtils.getFilesFromIndex(filesToAdd, filesToRemove, directories.getIndexFile());
        for (var file: filesToAdd.entrySet()) {
            commit.putFile(file.getKey(), file.getValue());
        }
        for (var file: filesToRemove.entrySet()) {
            commit.removeFile(file.getKey());
        }
        SerializationUtils.writeCommitToFile(commit, directories.getCommitsDir());
        updateHead(commit, currentBranchName, true);
        MyFileUtils.clearFile(directories.getIndexFile());
    }

    public List<Commit> log(String fromRevision) throws GitException {
        Commit fromCommit = headCommit;
        if (fromRevision != null) {
            while (fromCommit != null && !fromCommit.getCommitHash().equals(fromRevision)) {
                fromCommit = SerializationUtils.deserializeCommitFromFile(directories.getCommitFileName(fromCommit.getParentHash()));
            }
        }
        if (fromCommit == null) {
            throw new GitException("Wrong revision hash in log");
        }
        List<Commit> commitsInLog = new LinkedList<>();
        while (fromCommit != null) {
            commitsInLog.add(fromCommit);
            if (fromCommit.getParentHash() == null) {
                break;
            }
            fromCommit = SerializationUtils.deserializeCommitFromFile(directories.getCommitFileName(fromCommit.getParentHash()));
        }
        return commitsInLog;
    }

    public void checkoutFiles(List<String> filesToCheckout) throws GitException {
        for(String file: filesToCheckout) {
            checkoutFile(file);
        }
    }

    private void checkoutFile(String fileToCheckoutName) throws GitException {
        File fileToCheckout = new File(directories.getUserFileName(fileToCheckoutName));
        MyFileUtils.deleteLinesFromFile(directories.getIndexFile(), str -> !str.startsWith("add " + fileToCheckout.getAbsolutePath())
                                                                    && !str.startsWith("rm " + fileToCheckout.getAbsolutePath()));
        String hash = headCommit.getFileHash(directories.getUserFileName(fileToCheckoutName));
        if (hash == null) {
            throw new GitIncorrectInputException("Unknown to git file");
        }
        MyFileUtils.copyFile(directories.getObjectFileName(hash),
                            directories.getUserFileName(fileToCheckoutName));

    }

    public Map.Entry<Status, String> status(List<String> readyToCommit,
                       List<String> untrackedNewFiles,
                       List<String> untrackedModifiedFiles,
                       List<String> untrackedRemovedFiles) throws GitException {
        if (head == null) {
            return new AbstractMap.SimpleEntry<>(Status.PROJECT_IS_NOT_INITIALIZED, null);
//            return "Not initialized";
        }
        if (head.isDetached()) {
            return new AbstractMap.SimpleEntry<>(Status.HEAD_IS_DETACHED, null);
//            return null;
        }
        Map<String, String> filesToAdd = new HashMap<>();
        Map<String, String> filesToRemove = new HashMap<>();
        IndexUtils.getFilesFromIndex(filesToAdd, filesToRemove, directories.getIndexFile());
        for (var file: filesToRemove.entrySet()) {
            filesToAdd.remove(file.getKey());
        }
        readyToCommit.addAll(filesToAdd.keySet());
        untrackedNewFiles.addAll(DirectoriesUtils.getAllFilesInDirectory(directories.getWorkingDir(), path -> !path.startsWith(directories.getStorageDir())));

        for (var file: readyToCommit) {
            untrackedNewFiles.remove(file);
        }

        for (var file: headCommit.getFilesInCommit()) {
            if (filesToAdd.containsKey(file)) {
                continue;
            }
            if (!untrackedNewFiles.remove(file)) {
                untrackedRemovedFiles.add(file);
                continue;
            }

            String fileHash = headCommit.getFileHash(file);
            if (MyFileUtils.isFilesContentsDifferent(file, directories.getObjectFileName(fileHash))) {
                untrackedModifiedFiles.add(file);
            }
        }
        return new AbstractMap.SimpleEntry<>(Status.STATUS_CAN_BE_WRITTEN, head.getBranchName());
    }

    public void reset(String toRevision) throws GitException {
        if (!MyFileUtils.checkIfFileExists(directories.getCommitFileName(toRevision))) {
            throw new GitIncorrectInputException("This revision does not exist");
        }
        moveHead(toRevision, false);
    }

    public void checkoutRevision(String hash) throws GitException {
        if (!MyFileUtils.checkIfFileExists(directories.getCommitFileName(hash))) {
            throw new GitIncorrectInputException("Revision with this hash does not exist");
        }
        SerializationUtils.writeHeadToFile(head, directories.getBranchHeadFileName(head.getBranchName()));
        moveHead(hash, true);
    }

    private void moveHead(String revisionHash, Boolean needToDetachHead) throws GitException {
        updateHead(SerializationUtils.deserializeCommitFromFile(directories.getCommitFileName(revisionHash)), null, false);
        if (needToDetachHead) {
            head.setDetached(true);
        }
        updateFilesAccordingToHead();
    }

    private void updateFilesAccordingToHead() throws GitException {
        DirectoriesUtils.deleteAllFilesInDirectory(directories.getWorkingDir(), str -> !str.startsWith(directories.getStorageDir()));
        for (var entry: headCommit.getEntrySet()) {
            MyFileUtils.copyFile(directories.getObjectFileName(entry.getValue()), entry.getKey());
        }
        DirectoriesUtils.deleteEmptyDirsFromDirectory(directories.getWorkingDir());
    }

    public void checkoutBranch(String branchName) throws GitException {
        if (!MyFileUtils.checkIfFileExists(directories.getBranchHeadFileName(branchName))) {
            throw new GitIncorrectInputException("This branch does not exist");
        }
        head = SerializationUtils.deserializeHeadFromFile(directories.getBranchHeadFileName(branchName));
        headCommit = head.getCommit();
        currentBranchName = branchName;
        updateFilesAccordingToHead();
    }

    public void checkoutHeadN(int n) throws GitException {
        checkoutRevision(getRevision(n));
    }

    public void createBranch(String branchName) throws GitException {
        if (getBranches().contains(branchName)) {
            throw new GitIncorrectInputException("Branch with this name already exists");
        }
        updateHead(headCommit, branchName, !head.isDetached());
        currentBranchName = branchName;
    }

    public void removeBranch(String branchName) throws GitException {
        if (branchName.equals(currentBranchName)) {
            throw new GitIncorrectInputException("Unable to remove current branch");
        }
        if (!MyFileUtils.checkIfFileExists(directories.getBranchHeadFileName(branchName))) {
            throw new GitIncorrectInputException("Branch with this name does not exists");
        }
        MyFileUtils.deleteFile(directories.getBranchHeadFileName(branchName));
    }

    public List<String> getBranches() throws GitException {
        List<String> branches = DirectoriesUtils.getAllFilesInDirectory(directories.getBranchesDir(), path -> true);
        return branches.stream()
                .map(file -> file.substring(directories.getBranchesDir().length() + 1))
                .collect(Collectors.toList());
    }

    public Boolean isThisBranchExists(String branchName) {
        return MyFileUtils.checkIfFileExists(directories.getBranchHeadFileName(branchName));
    }

    public @NotNull String getRevision(int n) throws GitException {
        Commit currentCommit = headCommit;
        while (n != 0 && currentCommit != null) {
            currentCommit = SerializationUtils.deserializeCommitFromFile(directories.getCommitFileName(currentCommit.getParentHash()));
            n--;
        }
        if (currentCommit == null) {
            throw new GitIncorrectInputException("To large N to find revision");
        }
        return currentCommit.getCommitHash();
    }

}