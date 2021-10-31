package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class CreateBranchCommand extends AbstractCommand{
    private static final String action = "create";

    public CreateBranchCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        String branchName = GitArgumentParser.getOneArgumentFromList(arguments);
        if (branchName == null) {
            throw new GitIncorrectInputException("This command needs one argument");
        }
        git.createBranch(branchName);
        gitOutput.printBranchActionResult(branchName, action);
    }
}
