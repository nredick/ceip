import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static javax.swing.UIManager.getInt;

public class Main {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace <Subscription Key> with your valid subscription key.
    private static final String subscriptionKey = "567c11070cbf442b87327b54591fa99a";

    // You must use the same Azure region in your REST API method as you used to
    // get your subscription keys. For example, if you got your subscription keys
    // from the West US region, replace "westcentralus" in the URL
    // below with "westus".
    //
    // Free trial subscription keys are generated in the "westus" region.
    // If you use a free trial subscription key, you shouldn't need to change
    // this region.
    private static final String uriBase =
            "https://eastus.api.cognitive.microsoft.com/vision/v2.0/analyze";

    private static final String imageToAnalyze =
            "https://upload.wikimedia.org/wikipedia/commons/" +
                    "1/12/Broadway_and_Times_Square_by_night.jpg";

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("visualFeatures", "Description,Color");
            builder.setParameter("language", "en");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity requestEntity =
                    new StringEntity("{\"url\":\"" + imageToAnalyze + "\"}");
            request.setEntity(requestEntity);


            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);

                // parsing json code for readability

                System.out.println("Image: " + imageToAnalyze + "\n");

                String des = json.getJSONObject("description").getJSONArray("captions").getJSONObject(0).getString("text");
                System.out.println("Description: " + des);

                String format = json.getJSONObject("metadata").getString("format");
                System.out.println("File type: " + format);

                NumberFormat formatter = new DecimalFormat("#0.0000");
                double confidence = json.getJSONObject("description").getJSONArray("captions").getJSONObject(0).getDouble("confidence");
                System.out.println("Confidence: " + formatter.format(confidence));

                System.out.print("Dominant Color(s):\n");
                for(Object j : json.getJSONObject("color").getJSONArray("dominantColors")){
                    System.out.print(j + "\n");
                }

                System.out.print("Tags:\n");
                for(int i = 0; i < 10; i++) {
                    System.out.print(json.getJSONObject("description").getJSONArray("tags").get(i) + "\n");
                }


                //print json response: (as taken from quickstart sample)
                //System.out.println(json.toString(2));
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
}
