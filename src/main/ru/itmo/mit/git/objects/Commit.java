package ru.itmo.mit.git.objects;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.*;

public class Commit implements Serializable {
    private final Map<String, String> filesInCommit = new HashMap<>();
    private final String parentHash;
    private final String commitHash;
    private final String commitMessage;
    private final Date date;
    private final String author;

    public Commit(String commitMessage, String author, Date date,  String parentHash) {
        this.parentHash = parentHash;
        this.date = date;
        this.commitMessage = commitMessage;
        this.author = author;
        this.commitHash = DigestUtils.sha1Hex(parentHash + commitMessage + date + author);
    }

    public void putFile(String fileName, String fileHash) {
        filesInCommit.put(fileName, fileHash);
    }
    public void removeFile(String fileName) {
        filesInCommit.remove(fileName);
    }

    public String getFileHash(String fileName) {
        return filesInCommit.get(fileName);
    }

    public Set<String> getFilesInCommit() {
        return filesInCommit.keySet();
    }

    public Set<Map.Entry<String, String>> getEntrySet() {
        return filesInCommit.entrySet();
    }

    public String getCommitHash() {
        return commitHash;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getAuthor() {
        return author;
    }
    public Date getDate() {
        return date;
    }
    public String getCommitMessage() {
        return commitMessage;
    }

    public void addParentFiles(Commit parent) {
        for (var entry: parent.filesInCommit.entrySet()) {
            filesInCommit.put(entry.getKey(), entry.getValue());
        }
    }
}
