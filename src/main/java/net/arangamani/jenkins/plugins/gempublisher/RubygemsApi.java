package net.arangamani.jenkins.plugins.gempublisher;
import java.io.BufferedReader;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author: Kannan Manickam <me@arangamani.net>
 */
public class RubygemsApi {

    private final RubygemsCreds creds;

    private final String USER_AGENT = "JenkinsGemPlugin/1.0";
    private final String FILE_NAME = "/Users/kannanmanickam/Projects/self/jenkins_plugins/gem-publisher/work/workspace/test/test_rubygem-0.0.4.gem";

    @DataBoundConstructor
    public RubygemsApi(RubygemsCreds creds) {
        this.creds = creds;
    }

    public void testMethod() throws Exception{
        sendPostExample();
    }

    public void postGem() throws Exception {
        System.out.println("Posting gem... " + FILE_NAME);
        sendPost("https://rubygems.org/api/v1/gems", creds.getKey());
    }

    private void sendPost(String url, String key) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Authorization", key);

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();

        byte[] fileContents = read(FILE_NAME);

        os.write(fileContents);
        os.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print Result
        System.out.println(response.toString());

    }

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
        /*
         the above style is a bit tricky: it places bytes into the 'result' array;
         'result' is an output parameter;
         the while loop usually has a single iteration only.
        */
            }
            finally {
                input.close();
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        return result;
    }git
}
