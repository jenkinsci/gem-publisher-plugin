package net.arangamani.jenkins.gempublisher;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.CopyOnWrite;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
//import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Kannan Manickam <me@arangamani.net>
 */
public final class GemPublisher extends Recorder implements Describable<Publisher> {

    private static final Logger log = Logger
            .getLogger(GemPublisher.class.getName());
    public final String gemName;

    @Extension
    public static final GemDescriptor DESCRIPTOR = new GemDescriptor();

    public RubygemsApi api;

    /**
     *
     * @param gemName
     */
    @DataBoundConstructor
    public GemPublisher(String gemName) {
        this.gemName = gemName;
    }

    /**
     *
     * @param build
     * @param launcher
     * @param listener
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build,
                           Launcher launcher,
                           BuildListener listener)
            throws InterruptedException, IOException {
        api = new RubygemsApi(DESCRIPTOR.getCreds());
        try {
            StringBuilder builder = new StringBuilder();
            String gemPath;
            gemPath = builder.append(build.getModuleRoot()).append(File.separator)
                    .append(gemName).toString();
            log.info("Pushing gem " + gemName + " to rubygems.org");
            api.postGem(gemPath);
        }
        catch(Exception e){
            log.warning(e.getMessage());
            e.printStackTrace(listener.fatalError("failed to push gem: " + gemName));
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.STEP;
    }

    /**
     *
     * @return
     */
    @Override
    public GemDescriptor getDescriptor(){
        return DESCRIPTOR;
    }

    /**
     *
     * @return
     */
    public String getGemName() {
        return gemName;
    }

    /**
     *
     */
    public static final class GemDescriptor extends BuildStepDescriptor<Publisher> {

        @CopyOnWrite
        private volatile RubygemsCreds creds;

        /**
         *
         * @param clazz
         */
        public GemDescriptor(Class<? extends Publisher> clazz) {
            super(clazz);
            load();
        }

        /**
         *
         */
        public GemDescriptor() {
            this(GemPublisher.class);
        }

        /**
         *
         * @return
         */
        @Override
        public String getDisplayName() {
            return "Publish gems to rubygems.org";
        }

        /**
         *
         * @return
         */
        @Override
        public String getHelpFile() {
            return "/plugin/gem/help.html";
        }

        /**
         *
         * @param req
         * @param formData
         * @return
         * @throws FormException
         */
        @Override
        public GemPublisher newInstance(StaplerRequest req, net.sf.json.JSONObject formData) throws FormException {
            return (GemPublisher) req.bindJSON(clazz, formData);
        }

        /**
         *
         * @param req
         * @param json
         * @return
         * @throws FormException
         */
        @Override
        public boolean configure(StaplerRequest req, net.sf.json.JSONObject json) throws FormException {
            creds = req.bindParameters(RubygemsCreds.class, "creds.");
            save();
            return true;
        }

        /**
         *
         * @return
         */
        public RubygemsCreds getCreds() {
            return creds;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
    }
}