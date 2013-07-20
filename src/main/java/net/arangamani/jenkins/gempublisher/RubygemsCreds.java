package net.arangamani.jenkins.plugins.gempublisher;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class RubygemsCreds {
    private final String key;

    @DataBoundConstructor
    public RubygemsCreds(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
