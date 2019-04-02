import java.io.File;

//to import folder of files argument:
// 1. plus button; add a "Application"
// 2. "Run" > "Edit configurations" > "Main class" = App
// > "Program Arguments" = detect-labels /Users/nathalieredick/Desktop/samples > "Working Directory" =
// /Users/nathalieredick/IdeaProjects/AWS sample 2.0

public class App {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("Please provide at least one argument.");
            return;
        }
        if ("detect-labels".equals(args[0])) {
            DetectLabels detectLabels = new DetectLabels();
            detectLabels.run(args);
        } else {
            System.err.println("Unknown argument: " + args[0]);
            return;
        }
    }
}