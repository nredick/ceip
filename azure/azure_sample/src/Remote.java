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
import org.json.JSONObject;

public class Remote {

    private static final String subscriptionKey = "9f7acebb2f2548e280fce69f75cf8151";

    private static final String uriBase =
            "https://eastus.api.cognitive.microsoft.com/vision/v2.0/analyze";

    private static final String imageToAnalyze =
            "https://upload.wikimedia.org/wikipedia/commons/1/12/Broadway_and_Times_Square_by_night.jpg";

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
                // Format the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);

                //print json response: (as taken from quickstart sample)
                System.out.println(json.toString(2));

                // parsing json code for readability

                System.out.println("Image: " + imageToAnalyze + "\n");

                String des = json.getJSONObject("description").getJSONArray("captions").getJSONObject(0).getString("text");
                System.out.println("Description: " + des);

                String format = json.getJSONObject("metadata").getString("format");
                System.out.println("File type: " + format);

                NumberFormat formatter = new DecimalFormat("#0.0000");
                double confidence = json.getJSONObject("description").getJSONArray("captions").getJSONObject(0).getDouble("confidence");
                System.out.println("Confidence: " + formatter.format(confidence));

                if(json.getJSONObject("color").getJSONArray("dominantColors").length()>0) {
                    System.out.print("Dominant Color(s):\n");
                    for (Object j : json.getJSONObject("color").getJSONArray("dominantColors")) {
                        System.out.print(j + "\n");
                    }
                }

                System.out.print("Tags:\n");
                for(int i = 0; i < 10; i++) {
                    System.out.print(json.getJSONObject("description").getJSONArray("tags").get(i) + "\n");
                }
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
}