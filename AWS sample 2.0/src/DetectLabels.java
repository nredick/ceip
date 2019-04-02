import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DetectLabels {

    public void run(String[] args) {

        /*if (args.length < 2) {
            System.err.println("Please provide an image.");
            return;
        }*/

        File folder = new File(args[1]);
        File[] listings = folder.listFiles();
        //System.out.println(listings);
        int count = 1;
        if(listings != null){
            for(File f : listings){
                String imgPath = f.toString();
                byte[] bytes;
                try {
                    bytes = Files.readAllBytes(Paths.get(imgPath));
                } catch (IOException e) {
                    System.err.println("Failed to load image: " + e.getMessage());
                    return;
                }
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

                AmazonRekognition rekognition = ClientFactory.createClient();

                DetectLabelsRequest request = new DetectLabelsRequest()
                        .withImage(new Image().withBytes(byteBuffer))
                        .withMaxLabels(10);
                DetectLabelsResult result = rekognition.detectLabels(request);

                List<Label> labels = result.getLabels();
                System.out.println("Image #" + count + ": " + imgPath);
                for (Label label : labels) {
                    System.out.println(label.getName() + ": " + label.getConfidence());
                }
                count++;
                System.out.print("\n");
            }
        }

    }
}