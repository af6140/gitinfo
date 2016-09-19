package com.entertainment.bamboo.plugins.tasks.util;

/**
 * Created by dwang on 9/14/16.
 */
import java.io.File;
import java.io.IOException;

import com.entertainment.bamboo.plugins.tasks.model.GitCommitInfo;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.util.LinkedList;
import java.util.List;

public class GitRepoUtil {
    //final BuildLogger buildLogger = taskContext.getBuildLogger();
    public static Repository getRepository(String gitDir) {
        Repository repo = null;
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            System.out.println("Git dir : " + gitDir);
            repo = builder.setGitDir(new File(gitDir)).readEnvironment().findGitDir().build();


        } catch (IOException e) {
            System.err.println("Directory " + gitDir + " is not a .git directory");

        }
        return repo;
    }

    public static GitCommitInfo getCurrentCommit(Repository repo) {
        RevCommit commit =null;
        GitCommitInfo commitInfo = null;
        ObjectId lastCommitId = null;
        try {
            lastCommitId = repo.resolve(Constants.HEAD);
            if (lastCommitId ==null ) {
                return null;
            }
            RevWalk revWalk = new RevWalk(repo);
            commit = revWalk.parseCommit(lastCommitId);
            commitInfo = new GitCommitInfo();
            commitInfo.setCommiter(commit.getCommitterIdent().getName());
            commitInfo.setCommitDate(""+commit.getCommitTime());
            commitInfo.setCommitMessage(commit.getFullMessage().replace(System.lineSeparator(), "\\n"));
            commitInfo.setCommitHash(lastCommitId.name());
            Git git = new Git(repo);
            List<Ref> call = git.tagList().call();
            List<String> tags = new LinkedList<String>();
            boolean tagFound = false;
            for (Ref ref : call) {
                //System.out.println("Tag: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
                // fetch all commits for this tag
                LogCommand log = git.log();
                Ref peeledRef = repo.peel(ref);
                ObjectId tagId = null;
                if(peeledRef.getPeeledObjectId() != null) {
                    tagId = peeledRef.getPeeledObjectId();
                } else {
                    tagId = ref.getObjectId();
                }
                if (tagId.equals(lastCommitId) ){
                    tagFound = true;
                    String tagName = ref.getName();
                    String[] tagNameSpecs = tagName.split("/");
                    String shortTag = tagNameSpecs[tagNameSpecs.length-1];
                    tags.add(shortTag);
                }
            }
            if (tagFound) {
                commitInfo.setTagged(true);
                commitInfo.setTag(StringUtils.join(tags, ","));
            }

        } catch (IOException e) {

        } catch (GitAPIException e) {

        }
        return commitInfo;
    }
}
