package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class CommitCommand extends AbstractCommand{
    public CommitCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        String message = GitArgumentParser.getOneArgumentFromList(arguments);
        if (message == null) {
            throw new GitIncorrectInputException("This command needs one argument");
        }
        git.commit(message);
        gitOutput.printSuccess(GitConstants.COMMIT);
    }
}
