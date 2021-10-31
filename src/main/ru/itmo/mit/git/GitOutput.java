package ru.itmo.mit.git;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.itmo.mit.git.objects.*;

public class GitOutput {
    private PrintStream outputStream = System.out;

    private static final String initSuccess = "Project initialized";
    private static final String commitSuccess = "Files committed";
    private static final String addSuccess = "Add completed successful";
    private static final String rmSuccess = "Rm completed successful";
    private static final String checkoutSuccess = "Checkout completed successful";
    private static final String resetSuccess = "Reset successful";
    private static final String removeBranchSuccess = "Branch %s removed successfully";
    private static final String createBranchSuccess = "Branch %s created successfully\nYou can checkout it with 'checkout %s'";
    private static final String statusUpToDate = "Everything up to date";
    private static final String readyToCommit = "Ready to commit:\n";
    private static final String untrackedFiles = "Untracked files:\n";
    private static final String headIsDetached = "Error while performing status: Head is detached";
    private static final String currentBranch = "Current branch is '%s'";
    private static final String availableBranches = "Available branches:";


    public void setOutputStream(@NotNull PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public void printSuccess(String command) {
        switch (command) {
            case (GitConstants.INIT):
                outputStream.println(initSuccess);
                break;
            case(GitConstants.COMMIT):
                outputStream.println(commitSuccess);
                break;
            case (GitConstants.ADD):
                outputStream.println(addSuccess);
                break;
            case (GitConstants.RM):
                outputStream.println(rmSuccess);
                break;
            case (GitConstants.CHECKOUT):
                outputStream.println(checkoutSuccess);
                break;
            case (GitConstants.RESET):
                outputStream.println(resetSuccess);
                break;
        }
    }

    public void printBranchActionResult(String branchName, String action) {
        if (action.equals("create")) {
            outputStream.printf(createBranchSuccess, branchName, branchName);
            outputStream.println();
            return;
        }
        outputStream.printf(removeBranchSuccess, branchName);
        outputStream.println();
    }

    public void printLog(List<Commit> commitsInLog) {
        for (var commit: commitsInLog) {
            outputStream.println("Commit " + commit.getCommitHash());
            outputStream.println("Author: " + commit.getAuthor());
            outputStream.println("Date: " + commit.getDate() + "\n");
            outputStream.println(commit.getCommitMessage() + "\n");
        }
    }

    public void printStatus(Map.Entry<Status, String> entry,
                            List<String> readyToCommitFiles,
                            List<String> untrackedNewFiles,
                            List<String> untrackedModifiedFiles,
                            List<String> untrackedRemovedFiles,
                            String prefixNotToWrite) {
        switch (entry.getKey()) {
            case HEAD_IS_DETACHED:
                outputStream.println(headIsDetached);
                break;
            case PROJECT_IS_NOT_INITIALIZED:
                outputStream.println("Project is not initialized");
                break;
            case STATUS_CAN_BE_WRITTEN:
                outputStream.printf(currentBranch, entry.getValue());
                outputStream.println();
                if (readyToCommitFiles.isEmpty() && untrackedNewFiles.isEmpty()
                        && untrackedModifiedFiles.isEmpty() && untrackedRemovedFiles.isEmpty()) {
                    outputStream.println(statusUpToDate);
                    return;
                }
                if (!readyToCommitFiles.isEmpty()) {
                    outputStream.println(readyToCommit);
                    printFilesWithMessage("New files:", readyToCommitFiles, prefixNotToWrite);
                }
                if (!untrackedNewFiles.isEmpty()
                        || !untrackedModifiedFiles.isEmpty()
                        || !untrackedRemovedFiles.isEmpty()) {
                    outputStream.println(untrackedFiles);
                    printFilesWithMessage("New files:", untrackedNewFiles, prefixNotToWrite);
                    printFilesWithMessage("Modified files:", untrackedModifiedFiles, prefixNotToWrite);
                    printFilesWithMessage("Removed files:", untrackedRemovedFiles, prefixNotToWrite);
                }
        }
    }

    private void printFilesWithMessage(String message, Collection<String> files, String prefixNotToWrite) {
        if (files.isEmpty()) {
            return;
        }
        outputStream.println(message);
        printAllArrayElements("\t", files, prefixNotToWrite);
    }


    public void printAllArrayElements(String messageBeforeFile, Collection<String> arguments, String prefixNotToWrite) {
        for (var argument: arguments) {
            outputStream.println(messageBeforeFile + argument.substring(prefixNotToWrite.length()));
        }
        outputStream.println();
    }

    public void printBranches(List<String> branches) {
        outputStream.println(availableBranches);
        printAllArrayElements("", branches, "");
    }

    public void printError(String error) {
        outputStream.println(error);
    }
}
