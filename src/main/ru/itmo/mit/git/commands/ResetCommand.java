package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class ResetCommand extends AbstractCommand{
    public ResetCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        String toRevision = GitArgumentParser.getOneArgumentFromList(arguments);
        if (toRevision == null) {
            throw new GitIncorrectInputException("This command needs one argument");
        }
        git.reset(toRevision);
        gitOutput.printSuccess(GitConstants.RESET);
    }
}
