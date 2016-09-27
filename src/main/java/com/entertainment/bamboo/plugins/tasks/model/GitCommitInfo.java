package com.entertainment.bamboo.plugins.tasks.model;

/**
 * Created by dwang on 9/14/16.
 */
public class GitCommitInfo implements java.io.Serializable {
    private  String commitMessage;
    private  boolean isTagged;
    private  String commiter;
    private String tag;
    private String commitDate;
    private String commitHash;
    private boolean isAnnotated;

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public void setTagged(boolean tagged) {
        isTagged = tagged;
    }

    public String getCommiter() {
        return commiter;
    }

    public void setCommiter(String commiter) {
        this.commiter = commiter;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public boolean isAnnotated() {
        return isAnnotated;
    }

    public void setAnnotated(boolean annotated) {
        isAnnotated = annotated;
    }

    public String getPropertiesString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("author=").append(commiter).append(System.lineSeparator());
        buffer.append("sha1=").append(commitHash).append(System.lineSeparator());
        buffer.append("tagged=").append(isTagged).append(System.lineSeparator());
        buffer.append("annotated=").append(isAnnotated).append(System.lineSeparator());
        buffer.append("tag=").append( tag!=null ? tag : "").append(System.lineSeparator());
        buffer.append("ref=").append( tag!=null ? tag : "").append(System.lineSeparator());
        buffer.append("time=").append(commitDate).append(System.lineSeparator());
        buffer.append("message=").append(commitMessage != null ? commitMessage.replaceAll("[\\t\\n\\r]+"," ") : "").append(System.lineSeparator());
        return buffer.toString();
    }
}
