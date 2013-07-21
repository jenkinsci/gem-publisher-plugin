package net.arangamani.jenkins.gempublisher;
import java.io.BufferedReader;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class RubygemsApi {

    private static final Logger log = Logger
            .getLogger(RubygemsApi.class.getName());

    private final RubygemsCreds creds;

    private final String USER_AGENT = "JenkinsGemPlugin/1.0";

    /**
     *
     * @param creds the RubygemsCreds object that holds the
     *              credentials to communicate with rubygems.org
     */
    @DataBoundConstructor
    public RubygemsApi(RubygemsCreds creds) {
        this.creds = creds;
    }

    /**
     *
     * @param gemFile the absolute location of the gem file
     *
     * @return response from the POST request to post the gem
     *
     * @throws Exception
     */
    public String postGem(String gemFile) throws Exception {
        String postURL = "https://rubygems.org/api/v1/gems";
        String response;
        byte[] fileContents = read(gemFile);
        log.info("Posting gem file " + gemFile + " to " + postURL);
        response = doPost(postURL, creds.getKey(),
                "application/octet-stream", fileContents);
        return response;
    }

    /**
     * Makes a POST request to the specified URL with the specified
     * parameters. This can send binary date of specified content type.
     *
     * @param url the URL to make the request
     * @param key the API key to authenticate with rubygems.org
     * @param contentType the content type of the request body
     * @param body the body (binary date) of the request
     *
     * @return the String response from the POST request
     *
     * @throws Exception
     */
    private String doPost(String url, String key, String contentType, byte[] body)
            throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Authorization", key);
        con.setRequestProperty("Content-Type", contentType);
        con.setRequestProperty("Content-Length", Integer.toString(body.length));

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(body);
        os.close();

        int responseCode = con.getResponseCode();
        log.info("Response code for the POST request: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print Result
        log.info("Response from rubygems.org for POST: " + response.toString());
        return response.toString();
    }

    /**
     *
     * @param aInputFileName the input filename
     *
     * @return the bytes read from the given file
     *
     * @throws Exception
     */
    private byte[] read(String aInputFileName) throws Exception{
        File file = new File(aInputFileName);
        byte[] result = new byte[(int)file.length()];
        try {
            InputStream input = null;
            try {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while(totalBytesRead < result.length){
                    int bytesRemaining = result.length - totalBytesRead;
                    //input.read() returns -1, 0, or more :
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                    if (bytesRead > 0){
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
            }
            finally {
                input.close();
            }
        }
        catch (FileNotFoundException ex) {
            log.severe("File not found: " + aInputFileName);
        }
        catch (IOException ex) {
            log.severe("Can't read gem file: " + ex.getMessage());
        }
        return result;
    }
}
