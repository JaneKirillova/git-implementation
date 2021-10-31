package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.*;

import java.util.List;

public class CheckoutCommand extends AbstractCommand{
    public CheckoutCommand(MyGit git) {
        super(git);
    }

    @Override
    public void run(GitOutput gitOutput, List<String> arguments) throws GitException {
        var entry = GitArgumentParser.getArgumentsForCheckout(arguments, git);
        switch (entry.getKey()) {
            case ERROR:
                throw new GitIncorrectInputException("Incorrect arguments for command");
            case FILE:
                git.checkoutFiles(entry.getValue());
                gitOutput.printSuccess(GitConstants.CHECKOUT);
                break;
            case COMMIT_HASH:
                git.checkoutRevision(entry.getValue().get(0));
                gitOutput.printSuccess(GitConstants.CHECKOUT);
                break;
            case REVISION_NUMBER_FROM_HEAD:
                git.checkoutHeadN(Integer.parseInt(entry.getValue().get(0)));
                gitOutput.printSuccess(GitConstants.CHECKOUT);
                break;
            case BRANCH:
                git.checkoutBranch(entry.getValue().get(0));
                gitOutput.printSuccess(GitConstants.CHECKOUT);
                break;
        }
    }
}
