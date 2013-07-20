package net.arangamani.jenkins.plugins.gempublisher;
import hudson.Extension;
//import hudson.FilePath;
import hudson.Launcher;
//import hudson.Util;
import hudson.model.*;
import hudson.CopyOnWrite;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
//import hudson.util.CopyOnWriteList;
//import hudson.util.FormValidation;
//import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
//import org.kohsuke.stapler.StaplerResponse;

//import javax.servlet.ServletException;
import java.io.IOException;
//import java.io.PrintStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kannan Manickam <me@arangamani.net>
 */
public final class GemPublisher extends Recorder implements Describable<Publisher> {

    private static final Logger log = Logger
            .getLogger(GemPublisher.class.getName());
    public final String gemLocation;

    @Extension
    public static final GemDescriptor DESCRIPTOR = new GemDescriptor();

    @DataBoundConstructor
    public GemPublisher(String gemLocation) {
        this.gemLocation = gemLocation;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build,
                           Launcher launcher,
                           BuildListener listener)
            throws InterruptedException, IOException {
        //log(listener.getLogger(), "Testing Gem publisher");
        log.info("Kannan is testing: " + getGemLocation() + " >>>> " + DESCRIPTOR.getRubygemsCreds().getKey());
        return true;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.STEP;
    }

    @Override
    public GemDescriptor getDescriptor(){
        return DESCRIPTOR;
    }

    public String getGemLocation() {
        return gemLocation;
    }

    public static final class GemDescriptor extends BuildStepDescriptor<Publisher> {

        @CopyOnWrite
        private volatile RubygemsCreds gemcreds;

        public GemDescriptor(Class<? extends Publisher> clazz) {
            super(clazz);
            load();
        }

        public GemDescriptor() {
            this(GemPublisher.class);
        }

        @Override
        public String getDisplayName() {
            return "Publish gems to rubygems.org";
        }

        @Override
        public String getHelpFile() {
            return "/plugin/gem/help.html";
        }

        @Override
        public GemPublisher newInstance(StaplerRequest req, net.sf.json.JSONObject formData) throws FormException {
            return (GemPublisher) req.bindJSON(clazz, formData);
        }

        @Override
        public boolean configure(StaplerRequest req, net.sf.json.JSONObject json) throws FormException {
            gemcreds = req.bindParameters(RubygemsCreds.class, "gemcreds.");
            save();
            return true;
        }

        public RubygemsCreds getRubygemsCreds() {
            return gemcreds;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
    }
}