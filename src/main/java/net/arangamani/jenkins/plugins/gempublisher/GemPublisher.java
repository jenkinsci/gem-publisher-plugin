package net.arangamani.jenkins.plugins.gempublisher;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: kannanmanickam
 * Date: 7/14/13
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GemPublisher extends Recorder implements Describable<Publisher> {

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    @DataBoundConstructor
    public GemPublisher() {
        super();
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build,
                           Launcher launcher,
                           BuildListener listener)
            throws InterruptedException, IOException {
        //log(listener.getLogger(), "Testing Gem publisher");
        return true;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.STEP;
    }

    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public DescriptorImpl(Class<? extends Publisher> clazz) {
            super(clazz);
            load();
        }

        public DescriptorImpl() {
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
            GemPublisher pub = new GemPublisher();
            //req.bindParameters(pub, "s3.");
            //pub.getEntries().addAll(req.bindParametersToList(Entry.class, "s3.entry."));
            //pub.getUserMetadata().addAll(req.bindParametersToList(MetadataPair.class, "s3.metadataPair."));
            return pub;
        }

        @Override
        public boolean configure(StaplerRequest req, net.sf.json.JSONObject json) throws FormException {
            //profiles.replaceBy(req.bindParametersToList(S3Profile.class, "s3."));
            save();
            return true;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
    }
}