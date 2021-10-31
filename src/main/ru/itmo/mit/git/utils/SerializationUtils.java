package ru.itmo.mit.git.utils;

import org.jetbrains.annotations.NotNull;
import ru.itmo.mit.git.GitException;
import ru.itmo.mit.git.objects.Commit;
import ru.itmo.mit.git.objects.Head;

import java.io.*;

public class SerializationUtils {
    public static Commit deserializeCommitFromFile(@NotNull String fileName) throws GitException {
        try (FileInputStream fin = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fin)){
            return (Commit) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GitException("Problem while deserializing commit", e);
        }
    }

    public static void writeCommitToFile(Commit commit, String directory) throws GitException {
        try (FileOutputStream fout = new FileOutputStream(directory + File.separator + commit.getCommitHash());
             ObjectOutput out = new ObjectOutputStream(fout)){
            out.writeObject(commit);
        } catch (IOException e) {
            throw new GitException("Problem while serializing commit", e);
        }
    }

    public static Head deserializeHeadFromFile(@NotNull String file) throws GitException {
        try (FileInputStream fin = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fin)){
            return (Head) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GitException("Problem while deserializing head", e);
        }
    }

    public static void writeHeadToFile(Head head, String directory) throws GitException {

        try (FileOutputStream fout = new FileOutputStream(directory);
             ObjectOutput out = new ObjectOutputStream(fout)){
            out.writeObject(head);
        } catch (IOException e) {
            throw new GitException("Problem while serializing head", e);
        }
    }
}
