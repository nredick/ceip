import java.awt.*;
import java.io.File;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Local {
    public static final String subscriptionKey = "567c11070cbf442b87327b54591fa99a";

    public static final String uriBase =
            "https://eastus.api.cognitive.microsoft.com/vision/v2.0/analyze";
    static String print = "";

    public String getResults(){
        return print;
    }

    public static void main(String[] args) {
        try {
            File folder = new File(args[0]);
            File[] listings = folder.listFiles();

            int count = 1;
            if (listings != null) {
                for (File f : listings) {

                    //DefaultHttpClient client = new HttpClient(); this was deprecated
                    HttpClient httpclient = HttpClientBuilder.create().build();

                    URIBuilder builder = new URIBuilder(uriBase);
                    //request params
                    builder.setParameter("visualFeatures", "Description,Color");
                    builder.setParameter("language", "en");

                    URI uri = builder.build();
                    HttpPost request = new HttpPost(uri);

                    request.setHeader("Content-Type", "application/octet-stream"); // value: "application/json" gave a "code:" ["BadArgument"] response
                    request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

                    FileEntity reqEntityF = new FileEntity(f, ContentType.APPLICATION_OCTET_STREAM);
                    request.setEntity(reqEntityF);
                    HttpResponse response = httpclient.execute(request);
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        // Format and display the JSON response.
                        String jsonString = EntityUtils.toString(entity);
                        JSONObject json = new JSONObject(jsonString);
                        //System.out.println(json.toString(2));

                        // parsing json code for readability

                        System.out.println("\nImage #" + count + ": " + f);
                        print+="\nImage #" + count + ": " + f;
                        count++;

                        if (json.getJSONObject("description").getJSONArray("captions").length() > 0) {
                            String des = json.getJSONObject("description").getJSONArray("captions").getJSONObject(0).getString("text");
                            System.out.println("Description: " + des);
                            print+="\nDescription: " + des;

                            NumberFormat formatter = new DecimalFormat("#0.0000");
                            double confidence = json.getJSONObject("description").getJSONArray("captions").getJSONObject(0).getDouble("confidence");
                            System.out.println("Confidence: " + formatter.format(confidence));
                            print+="\nConfidence: " + formatter.format(confidence);
                        }

                        String format = json.getJSONObject("metadata").getString("format");
                        System.out.println("File type: " + format);
                        print+="\nFile type: " + format;

                        System.out.print("Dominant Color(s):");
                        print+="\nDominant Color(s):\n";
                        for (Object j : json.getJSONObject("color").getJSONArray("dominantColors")) {
                            System.out.print(j + "\n");
                            print+=j+"\n";
                        }
                        if (json.getJSONObject("description").getJSONArray("tags").length() >= 10) {
                            System.out.print("Tags:\n");
                            print+="Tags:\n";
                            for (int i = 0; i < 10; i++) {
                                System.out.print(json.getJSONObject("description").getJSONArray("tags").get(i) + "\n");
                                print+=json.getJSONObject("description").getJSONArray("tags").get(i) + "\n";
                            }
                        } else {
                            System.out.print("Tags:\n");
                            print+="Tags:\n";
                            for (Object o : json.getJSONObject("description").getJSONArray("tags")) {
                                System.out.print(o + "\n");
                                print+=o + "\n";
                            }
                        }
                        //JOptionPane.showMessageDialog(frame, print);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}