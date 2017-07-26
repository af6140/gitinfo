package com.entertainment.bamboo.plugins.tasks;

import com.atlassian.bamboo.specs.api.model.AtlassianModuleProperties;
import com.atlassian.bamboo.specs.api.model.task.TaskProperties;
import com.atlassian.bamboo.specs.api.validators.common.ValidationContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

import static com.atlassian.bamboo.specs.api.validators.common.ImporterUtils.checkThat;

/**
 * Created by dwang on 7/26/17.
 */
@Immutable
public class GitInfoTaskProperties extends TaskProperties {
    @NotNull
    private String gitDir;
    @NotNull
    private String gitinfoFile;

    private static final AtlassianModuleProperties MODULE_PROPERTIES =
            new AtlassianModuleProperties("com.entertainment.bamboo.plugins.tasks.GitInfoTask:gitinfotask");

    public GitInfoTaskProperties() {
        validate();
    }

    @NotNull
    public String getGitDir() {
        return gitDir;
    }

    public void setGitDir(@NotNull String gitDir) {
        this.gitDir = gitDir;
    }

    @NotNull
    public String getGitinfoFile() {
        return gitinfoFile;
    }

    public void setGitinfoFile(@NotNull String gitinfoFile) {
        this.gitinfoFile = gitinfoFile;
    }

    @Override
    public void validate() {
        super.validate();
        final ValidationContext context = ValidationContext.of("GitinfoTask");
        checkThat(context, StringUtils.isNotBlank(gitDir), "Repository Directory is not defined");
        checkThat(context, StringUtils.isNotBlank(gitinfoFile), "Output file is not defined");
    }

    @NotNull
    @Override
    public AtlassianModuleProperties getAtlassianPlugin() {
        return MODULE_PROPERTIES;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GitInfoTaskProperties that = (GitInfoTaskProperties) o;

        if (!gitDir.equals(that.gitDir)) return false;
        return gitinfoFile.equals(that.gitinfoFile);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + gitDir.hashCode();
        result = 31 * result + gitinfoFile.hashCode();
        return result;
    }
}
