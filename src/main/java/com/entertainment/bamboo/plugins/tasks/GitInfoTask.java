package com.entertainment.bamboo.plugins.tasks;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.*;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Repository;

import org.jetbrains.annotations.NotNull;

import com.entertainment.cicd.util.GitInfoUtil;
import com.entertainment.cicd.util.GitCommitInfo;

/**
 * Created by dwang on 9/14/16.
 */
public class GitInfoTask implements TaskType{

    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        ConfigurationMap configurationMap = taskContext.getConfigurationMap();
        String repoDir = configurationMap.get(GitInfoTaskConfigurator.GITINFO_REPODIR);
        if(repoDir==null || repoDir.isEmpty()) {
            repoDir= taskContext.getWorkingDirectory().getAbsolutePath();
        }
        String gitDir = repoDir + File.separator+ ".git";
        Repository repo = GitInfoUtil.getRepository(gitDir, 3);

        if (repo ==null ){
            buildLogger.addErrorLogEntry("Directory " + repoDir + " is not a git repository");
            return failed(taskContext);
        }
        GitCommitInfo commitInfo = GitInfoUtil.getCurrentCommit(repo);

        if (commitInfo==null) {
            throw new TaskException("Not a valid git repository, cannot find commit information");
        }

        String output = configurationMap.get(GitInfoTaskConfigurator.GITINFO_OUTPUT);
        if(output==null || output.isEmpty()) {
            output = taskContext.getWorkingDirectory().getAbsolutePath()+File.separator+".gitinfo";
        }

        File f = new File(output);
        try {
            FileUtils.writeStringToFile(f, commitInfo.getPropertiesString());
            return success(taskContext);
        } catch (IOException e) {
            return failed(taskContext);
        }
    }

    public TaskResult success(TaskContext taskContext) {
        return TaskResultBuilder.newBuilder(taskContext).success().build();
    }

    public TaskResult failed(TaskContext taskContext) {
        return TaskResultBuilder.newBuilder(taskContext).failed().build();
    }

}
