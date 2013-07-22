package net.arangamani.jenkins.gempublisher;

import hudson.FilePath;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class GemFileChooser {
    private static final Logger log = Logger
            .getLogger(GemPublisher.class.getName());

    public static String chooseFile(String gemName, FilePath currentDir) {

        ArrayList<FilePath> matchedFiles = new ArrayList<FilePath>();
        log.info("Checking for gem files in directory: " + currentDir);
        try {
            List<FilePath> faFiles = currentDir.list();

            for (int i = 0; i < faFiles.size(); i++) {
                FilePath filePath = faFiles.get(i);
                if(filePath.getName().startsWith(gemName) && filePath.getName().endsWith(".gem")) {
                    matchedFiles.add(filePath);
                }
            }
            long modifiedTime = 0;
            FilePath chosenFile = null;
            for (FilePath fPath : matchedFiles) {
                if(fPath.lastModified() > modifiedTime) {
                    chosenFile = fPath;
                    modifiedTime = fPath.lastModified();
                }
            }
            log.info("Chosen gem file to publish: " + chosenFile.getName());
            if(chosenFile != null)
                return chosenFile.getName();
        }
        catch(Exception e) {
            log.severe("Error in choosing file: " + e.getMessage());
        }
        return null;
    }
}
