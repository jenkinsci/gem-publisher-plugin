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

    @DataBoundConstructor
    public RubygemsApi(RubygemsCreds creds) {
        this.creds = creds;
    }

    public void postGem(String gemFile) throws Exception {
        System.out.println("Posting gem... " + gemFile);
        byte[] fileContents = read(gemFile);
        sendPost("https://rubygems.org/api/v1/gems", creds.getKey(), fileContents);
    }

    private void sendPost(String url, String key, byte[] body) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Authorization", key);
        con.setRequestProperty("Content-Type", "application/octet-stream");
        con.setRequestProperty("Content-Length", Integer.toString(body.length));

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(body);
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("Response code for the POST request: " + responseCode);

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
    }
}
