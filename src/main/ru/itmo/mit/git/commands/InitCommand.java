package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class InitCommand extends AbstractCommand {
    public InitCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        if (!GitArgumentParser.isArgsCorrectForCommandWithoutArgs(arguments)) {
            throw new GitIncorrectInputException("This command does not need arguments");
        }
        git.init();
        gitOutput.printSuccess(GitConstants.INIT);
    }
}
