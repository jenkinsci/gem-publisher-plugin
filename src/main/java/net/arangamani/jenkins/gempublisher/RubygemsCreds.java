package net.arangamani.jenkins.gempublisher;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class RubygemsCreds {
    private final String key;

    /**
     * The constructor
     *
     * @param key The API key to authenticate with Rubygems
     */
    @DataBoundConstructor
    public RubygemsCreds(String key) {
        this.key = key;
    }

    /**
     * Gets the rubygems.org API key
     *
     * @return the rubygems.org API key
     */
    public String getKey() {
        return key;
    }
}
