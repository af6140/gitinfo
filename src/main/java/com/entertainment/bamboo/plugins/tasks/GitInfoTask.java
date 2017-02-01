package com.entertainment.bamboo.plugins.tasks;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

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
        if(repoDir==null || repoDir.trim().isEmpty()) {
            buildLogger.addBuildLogEntry("repo dir is set empty, use current working directory as repo directory");
            repoDir= taskContext.getWorkingDirectory().getAbsolutePath();
        }else {
            repoDir = taskContext.getWorkingDirectory().getAbsolutePath()+"/"+repoDir;
        }
        Path p = FileSystems.getDefault().getPath(repoDir).normalize();
        Repository repo = GitInfoUtil.getRepository(p.toString(), 2);

        if (repo ==null ){
            buildLogger.addErrorLogEntry("Directory " + p.toString() + " is not a git repository");
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
