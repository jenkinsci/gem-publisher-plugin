package net.arangamani.jenkins.plugins.gempublisher;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class RubygemsApi {

    private final RubygemsCreds creds;

    @DataBoundConstructor
    public RubygemsApi(RubygemsCreds creds) {
        this.creds = creds;
    }
}
