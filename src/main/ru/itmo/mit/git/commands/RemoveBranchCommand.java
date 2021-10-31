package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class RemoveBranchCommand extends AbstractCommand{
    private static final String action = "remove";

    public RemoveBranchCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        String branchName = GitArgumentParser.getOneArgumentFromList(arguments);
        if (branchName == null) {
            throw new GitIncorrectInputException("This command needs one argument");
        }
        git.removeBranch(branchName);
        gitOutput.printBranchActionResult(branchName, action);
    }
}
