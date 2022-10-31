import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


public class ScriptToCreateValuesMap {
    public static final Integer[] Y = {589, 617};
    public static final Integer[] X = {149,219,289,364,439};
    public static final Integer[] SIZESRANK = {31, 31};
    public static final Integer[] SIZESSUIT = {23, 21};

    public static Map<String, String> getValuesMapFromFileName() throws IOException {
        Map<String, String> mapToSerialize = new HashMap<>();

        File testDir = new File("src\\test\\resources\\imgs");
        for (File file : testDir.listFiles()
        ) {
            BufferedImage img = ImageIO.read(file);
            Recognizer rf = new Recognizer(img);
            mapToSerialize.put("blank", rf.invokeGetBinaryString(429,218,25,27));

            char[] nameCharArray = file.getName().replaceFirst("[.][^.]+$", "").replace("10", "T").toCharArray();
            for (int i = 0; i < nameCharArray.length; i++) {
                mapToSerialize.put(nameCharArray[i]=='T'? "10" : String.valueOf(nameCharArray[i]), rf.invokeGetBinaryString(X[i / 2], i % 2 == 0 ? Y[0] : Y[1], i % 2 == 0 ?SIZESRANK[0] : SIZESSUIT[0], i % 2 == 0 ?SIZESRANK[0]:SIZESSUIT[0]));
            }
        }
        return mapToSerialize;
    }

    public static void serializeValuesMap(Map<String, String> map) {
        try (ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream("map.out"))) {
            os.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
