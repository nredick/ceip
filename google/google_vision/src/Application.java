// Imports the Google Cloud client library

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) throws Exception {
        // Instantiates a client
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate
            File folder = new File(args[0]);
            File[] listings = folder.listFiles();

            // Reads the image file into memory
            int count = 1;
            if(listings != null) {
                for (File f : listings) {
                    if(!(f.toString().contains("/."))){
                        System.out.println("\nImage #" + count + ": \n");
                        count++;
                        Path path = Paths.get(f.toString());
                        byte[] data = Files.readAllBytes(path);
                        ByteString imgBytes = ByteString.copyFrom(data);

                        // Builds the image annotation request
                        List<AnnotateImageRequest> requests = new ArrayList<>();
                        Image img = Image.newBuilder().setContent(imgBytes).build();
                        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
                        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                                .addFeatures(feat)
                                .setImage(img)
                                .build();
                        requests.add(request);

                        // Performs label detection on the image file
                        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
                        List<AnnotateImageResponse> responses = response.getResponsesList();

                        for (AnnotateImageResponse res : responses) {
                            if (res.hasError()) {
                                System.out.printf("Error: %s\n", res.getError().getMessage());
                                return;
                            }

                            for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                                annotation.getAllFields().forEach((k, v) ->
                                        System.out.printf("%s : %s\n", k, v.toString()));
                            }
                        }

                    }
                }
            }

        }
    }
}
