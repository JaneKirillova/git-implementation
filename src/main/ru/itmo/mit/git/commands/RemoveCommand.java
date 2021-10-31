package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.GitConstants;
import ru.itmo.mit.git.GitException;
import ru.itmo.mit.git.GitOutput;
import ru.itmo.mit.git.MyGit;

import java.util.List;

public class RemoveCommand extends AbstractCommand {
    public RemoveCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        git.remove(arguments);
        gitOutput.printSuccess(GitConstants.RM);
    }
}
