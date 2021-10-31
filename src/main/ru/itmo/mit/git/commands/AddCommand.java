package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.GitConstants;
import ru.itmo.mit.git.GitException;
import ru.itmo.mit.git.GitOutput;
import ru.itmo.mit.git.MyGit;

import java.util.List;

public class AddCommand extends AbstractCommand {
    public AddCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        git.add(arguments);
        gitOutput.printSuccess(GitConstants.ADD);
    }
}
