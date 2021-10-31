package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatusCommand extends AbstractCommand{
    public StatusCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        if (!GitArgumentParser.isArgsCorrectForCommandWithoutArgs(arguments)) {
            throw new GitIncorrectInputException("This command does not need arguments");
        }
        List<String> readyToCommitFiles = new LinkedList<>();
        List<String> untrackedNewFiles = new LinkedList<>();
        List<String> untrackedModifiedFiles = new LinkedList<>();
        List<String> untrackedRemovedFiles = new LinkedList<>();
        Map.Entry<Status, String> entry = git.status(readyToCommitFiles, untrackedNewFiles, untrackedModifiedFiles, untrackedRemovedFiles);
        gitOutput.printStatus(entry,
                readyToCommitFiles,
                untrackedNewFiles,
                untrackedModifiedFiles,
                untrackedRemovedFiles,
                git.getWorkingDir() + "/");
    }
}
