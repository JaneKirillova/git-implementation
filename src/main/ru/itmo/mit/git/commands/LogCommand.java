package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;
import ru.itmo.mit.git.objects.*;

import java.util.List;

public class LogCommand extends AbstractCommand{
    public LogCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        if (GitArgumentParser.isArgsCorrectForCommandWithoutArgs(arguments)) {
            List<Commit> commits = git.log(null);
            gitOutput.printLog(commits);
        } else {
            String fromRevision = GitArgumentParser.getOneArgumentFromList(arguments);
            if (fromRevision == null) {
                throw new GitIncorrectInputException("Incorrect arguments for command");
            }
            List<Commit> commits = git.log(fromRevision);
            gitOutput.printLog(commits);
        }
    }
}
