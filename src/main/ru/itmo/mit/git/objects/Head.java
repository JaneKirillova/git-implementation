package ru.itmo.mit.git.objects;

import java.io.Serializable;

public class Head implements Serializable {
    private final Commit commit;
    private Boolean isDetached = false;
    private final String branchName;

    public Head(Commit commit, String branchName) {
        this.commit = commit;
        this.branchName = branchName;
    }

    public Boolean isDetached() {
        return isDetached;
    }

    public String getBranchName() {
        return branchName;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setDetached(Boolean detached) {
        isDetached = detached;
    }

}
