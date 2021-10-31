package ru.itmo.mit.git.commands;

import ru.itmo.mit.git.GitException;
import ru.itmo.mit.git.GitOutput;
import ru.itmo.mit.git.MyGit;

import java.util.List;

public abstract class AbstractCommand {
    MyGit git;
    public AbstractCommand(MyGit git) {
        this.git = git;
    }
    public abstract void run(GitOutput gitOutput, List<String> arguments) throws GitException;
}
