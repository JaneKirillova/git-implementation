package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class ShowBranchesCommand extends AbstractCommand{
    public ShowBranchesCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        if (!GitArgumentParser.isArgsCorrectForCommandWithoutArgs(arguments)) {
            throw new GitIncorrectInputException("This command does not need arguments");
        }
        gitOutput.printBranches(git.getBranches());
    }
}
