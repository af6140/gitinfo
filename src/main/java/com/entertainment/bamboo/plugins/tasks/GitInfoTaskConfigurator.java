package com.entertainment.bamboo.plugins.tasks;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskConfiguratorHelper;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Set;




/**
 * Created by dwang on 9/14/16.
 */
@Component
public class GitInfoTaskConfigurator extends AbstractTaskConfigurator {
    //protected final EncryptionService encryptionService;

    public static final String GITINFO_REPODIR = "gitinfo_repodir";
    public static final String GITINFO_OUTPUT = "gitinfo_file";

    private static final Set<String> FIELDS = ImmutableSet.of(
            GITINFO_REPODIR,
            GITINFO_OUTPUT
    );

    @Autowired
    public GitInfoTaskConfigurator(@ComponentImport final TaskConfiguratorHelper taskConfiguratorHelper) {
        this.taskConfiguratorHelper = taskConfiguratorHelper;
        //this.encryptionService = encryptionService;
    }
    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(config, params, FIELDS);
        return config;
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        super.validate(params, errorCollection);
        final String repoDir = params.getString(GitInfoTaskConfigurator.GITINFO_REPODIR);
        if (!StringUtils.isEmpty(repoDir) && repoDir.trim().length()>0) {
            File f = new File(repoDir);
            if (f.isAbsolute()) {
                errorCollection.addError(GITINFO_REPODIR, "is a absolute path");
                return;
            }
            if (!f.isDirectory()) {
                errorCollection.addError(GITINFO_REPODIR, "Is not a directory");
                return;
            }
        }

        final String outFile = params.getString(GitInfoTaskConfigurator.GITINFO_OUTPUT);
        if (!StringUtils.isEmpty(outFile) && outFile.trim().length()>0) {
            File f = new File(outFile);
            if(f.isAbsolute()) {
                errorCollection.addError(GITINFO_OUTPUT, "is a absolute path");
                return;
            }
        }

    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS);
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS);
    }

}
