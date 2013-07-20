package net.arangamani.jenkins.gempublisher;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class RubygemsCreds {
    private final String key;

    /**
     *
     * @param key The API key to authenticate with Rubygems
     */
    @DataBoundConstructor
    public RubygemsCreds(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return key;
    }
}
