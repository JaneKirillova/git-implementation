package ru.itmo.mit.git;

import org.jetbrains.annotations.NotNull;
import ru.itmo.mit.git.commands.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitCliImpl implements GitCli {
    MyGit git;
    GitOutput gitOutput = new GitOutput();
    Map<String, AbstractCommand> commandManager = new HashMap<>();

    public GitCliImpl(String workingDir) {
        git = new MyGit(workingDir);
        commandManager.put(GitConstants.INIT, new InitCommand(git));
        commandManager.put(GitConstants.ADD, new AddCommand(git));
        commandManager.put(GitConstants.RM, new RemoveCommand(git));
        commandManager.put(GitConstants.COMMIT, new CommitCommand(git));
        commandManager.put(GitConstants.LOG, new LogCommand(git));
        commandManager.put(GitConstants.RESET, new ResetCommand(git));
        commandManager.put(GitConstants.STATUS, new StatusCommand(git));
        commandManager.put(GitConstants.CHECKOUT, new CheckoutCommand(git));
        commandManager.put(GitConstants.BRANCH_CREATE, new CreateBranchCommand(git));
        commandManager.put(GitConstants.BRANCH_REMOVE, new RemoveBranchCommand(git));
        commandManager.put(GitConstants.SHOW_BRANCHES, new ShowBranchesCommand(git));
    }

    @Override
    public void runCommand(@NotNull String command, @NotNull List<@NotNull String> arguments) throws GitException {
        AbstractCommand commandToRun = commandManager.get(command);
        try {
            commandToRun.run(gitOutput, arguments);
        } catch (GitIncorrectInputException e) {
            gitOutput.printError(e.getMessage());
        }

    }

    @Override
    public void setOutputStream(@NotNull PrintStream outputStream) {
        gitOutput.setOutputStream(outputStream);
    }

    @Override
    public @NotNull String getRelativeRevisionFromHead(int n) throws GitException {
        return git.getRevision(n);
    }
}