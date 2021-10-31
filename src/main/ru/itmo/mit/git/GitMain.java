package ru.itmo.mit.git;

import java.util.Arrays;
import java.util.Collections;

public class GitMain {
    public static void main(String[] args) {
        GitCliImpl gitCli = new GitCliImpl(System.getProperty("user.dir"));
        try {
            if (args.length == 1) {
                gitCli.runCommand(args[0], Collections.emptyList());
            }
            gitCli.runCommand(args[0], Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
        } catch (GitException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
