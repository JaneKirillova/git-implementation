package ru.itmo.mit.git;

import java.util.*;

public class GitArgumentParser {
    private static final String argumentForCheckoutFiles = "--";
    private static final String beginForCheckoutOnCommitNFromHead = "HEAD~";
    private static final int amountOfSymbolsToPass = beginForCheckoutOnCommitNFromHead.length();

    public static boolean isArgsCorrectForCommandWithoutArgs(List<String> arguments) {
        return arguments.isEmpty();
    }

    public static String getOneArgumentFromList(List<String> arguments) {
        if (arguments.size() != 1) {
            return null;
        }
        return arguments.get(0);
    }

    public static Map.Entry<CheckoutOption, List<String>> getArgumentsForCheckout(List<String> arguments, MyGit git) {
        if (arguments.size() >= 2) {
            if (!arguments.get(0).equals(argumentForCheckoutFiles)) {
                return new AbstractMap.SimpleEntry<>(CheckoutOption.ERROR, null);
            }
            return new AbstractMap.SimpleEntry<>(CheckoutOption.FILE, arguments.subList(1, arguments.size()));
        }
        if (arguments.size() != 1) {
            return new AbstractMap.SimpleEntry<>(CheckoutOption.ERROR, null);
        }
        String argument = arguments.get(0);
        if (git.isThisBranchExists(argument)) {
            return new AbstractMap.SimpleEntry<>(CheckoutOption.BRANCH, Collections.singletonList(argument));
        }

        if (argument.startsWith(beginForCheckoutOnCommitNFromHead)) {
            return new AbstractMap.SimpleEntry<>(CheckoutOption.REVISION_NUMBER_FROM_HEAD, Collections.singletonList(argument.substring(amountOfSymbolsToPass)));
        }
        return new AbstractMap.SimpleEntry<>(CheckoutOption.COMMIT_HASH, Collections.singletonList(argument));
    }
}
