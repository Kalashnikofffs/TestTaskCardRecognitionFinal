import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class PostFlopRecognition {
    public static void main(String[] args){
        File[] listfiles = new File(args.length<1? "src\\main\\resources\\imgs": args[0]).listFiles();
        try {
            Map<String,String> patternMap = Recognizer.deserializeValueMap(new FileInputStream("map.out"));
            Arrays.stream(Objects.requireNonNull(listfiles))
                    .map(input -> {
                        StringBuilder results = new StringBuilder();
                        try {
                            Recognizer r = new Recognizer(ImageIO.read(input));
                            results.append(input.getName()).append("----->>>>>").append(r.recognize(patternMap));
                        } catch (IOException e) {
                            System.out.println("WrongFileFormat "+input.getName());
                        }
                        return results.toString();
                    }).forEach(System.out::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

