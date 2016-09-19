package ut.com.entertainment.bamboo.plugins.gitinfo;

import com.entertainment.bamboo.plugins.tasks.model.GitCommitInfo;
import com.entertainment.bamboo.plugins.tasks.util.GitRepoUtil;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;

/**
 * Created by dwang on 9/15/16.
 */
public class GitRepoUtilTest extends TestCase {

    private String testFixturesDir = "./target/test-fixtures";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        File f = new File(this.testFixturesDir);
        if (!f.exists()) {
            f.mkdir();
        }

        String repoDir = this.testFixturesDir+File.separator+"testrepo1";
        File repoFile = new File(repoDir);
        if(!repoFile.exists()) {
            System.out.println("Creating directory for tesstrepo1: " + repoDir);
            repoFile.mkdir();

            Git git = Git.init().setDirectory(repoFile).call();

            File testFile = new File(repoDir + File.separator + "test.txt");
            testFile.createNewFile();
            git.add().addFilepattern("test.txt").call();
            git.commit().setMessage("Test file added.\n1:a\n2:b").call();
            System.out.println("Test file commited");

            git.tag().setName("v0.0.1").setAnnotated(false).call();
        }


        String repoDir2 = this.testFixturesDir+File.separator+"testrepo2";
        File repoFile2 = new File(repoDir2);
        if(!repoFile2.exists()) {
            System.out.println("Creating directory for tesstrepo2: " + repoDir2);
            repoFile2.mkdir();

            Git git = Git.init().setDirectory(repoFile2).call();

            File testFile = new File(repoDir + File.separator + "test.txt");
            testFile.createNewFile();
            git.add().addFilepattern("test.txt").call();
            git.commit().setMessage("Test file added.\n1:a\n2:b").call();
            System.out.println("Test file  commited");

            git.tag().setName("v0.1.1").setAnnotated(false).call();

            File testFile2 = new File(repoDir + File.separator + "test2.txt");
            testFile2.createNewFile();
            git.add().addFilepattern("test2.txt").call();
            git.commit().setMessage("Test file 2 added.\n1:a\n2:b").call();

            // use detached head
            git.checkout().setName("v0.1.1").call();
        }

    }

    public void testGetRepository() {
        Repository repo = GitRepoUtil.getRepository(this.testFixturesDir+File.separator+"testrepo1"+File.separator+".git");
        assertNotNull(repo);
        System.out.println("Repository is read");
        GitCommitInfo info = GitRepoUtil.getCurrentCommit(repo);
        assertNotNull(info);
        assertTrue(info.isTagged());
        assertTrue(info.getCommitMessage().contains("Test file added."));
        assertTrue(info.getTag().contains("v0.0.1"));
    }

    public void testGetRepositoryDetached() {
        Repository repo = GitRepoUtil.getRepository(this.testFixturesDir+File.separator+"testrepo2"+File.separator+".git");
        assertNotNull(repo);
        System.out.println("Repository 2 is read");
        GitCommitInfo info = GitRepoUtil.getCurrentCommit(repo);
        //System.out.println("commit info: "+info.getPropertiesString());
        assertNotNull(info);
        assertTrue(info.isTagged());
        assertTrue(info.getCommitMessage().contains("Test file added."));
        assertTrue(info.getTag().contains("v0.1.1"));
    }

    @Override
    protected void tearDown() throws Exception {
        String repoDir = this.testFixturesDir+File.separator+"testrepo1";
        File repoFile = new File(repoDir);
        if (repoFile.exists() && repoFile.isDirectory()) {
            System.out.println("Delete " + repoDir);
            //FileUtils.deleteDirectory(repoFile);
        }

        repoDir = this.testFixturesDir+File.separator+"testrepo2";
        repoFile = new File(repoDir);
        if (repoFile.exists() && repoFile.isDirectory()) {
            System.out.println("Delete " + repoDir);
            //FileUtils.deleteDirectory(repoFile);
        }

    }
}
