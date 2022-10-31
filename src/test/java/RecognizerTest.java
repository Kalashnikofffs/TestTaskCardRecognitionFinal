import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecognizerTest {


    RecognizerTest() throws IOException {
    }

    /*
    Данный файл не является тестовым порытием. Просто скретчи для удобства разработки.
     */

    File testDir = new File("src\\test\\resources\\imgs");
    File negTestDir = new File("src\\test\\resources\\wrong");

    private final BufferedImage testImg = ImageIO.read(testDir.listFiles()[0]);

    private final Map<String, String> VALUEMAP = Recognizer.deserializeValueMap(new FileInputStream("map.out"));


    @org.junit.Test
    void getBinaryStringVisualTest() throws IOException {
        System.out.println(testImg.getData());

        new Recognizer(testImg).invokeGetBinaryString(149, 617, 27, 27);

    }

    @Test
    void serialize() throws IOException {
        ScriptToCreateValuesMap.serializeValuesMap(ScriptToCreateValuesMap.getValuesMapFromFileName());
    }


    @Test
    Map<String, String> testDeserializationOfValueMap() throws FileNotFoundException {
        return Recognizer.deserializeValueMap(new FileInputStream("src\\main\\resources\\valuesMap.out"));
    }

    @Test
    void visualTestValueMap() throws IOException {
        ScriptToCreateValuesMap.getValuesMapFromFileName().keySet().forEach(key -> {
            assert VALUEMAP != null;
            System.out.println(VALUEMAP.get(key) + "\n_________Is this right value   =>>> \n" + key + "\n\n");
        });
    }

    @Test
    void mainTest() {


        StringBuilder results = new StringBuilder();
        int counter = 0;
        for (File file : testDir.listFiles()
        ) {
            try {
                BufferedImage img = ImageIO.read(file);
                Recognizer r = new Recognizer(img);
                if (file.getName().replaceFirst("[.][^.]+$", "").equals(r.recognize(VALUEMAP)) && !file.getName().equals("error.txt")) {
                    System.out.println("Passed  " + file.getName() + "  " + r.recognize(VALUEMAP));
                    counter++;
                } else {
                    System.out.println("Not Passed" + file.getName().replaceFirst("[.][^.]+$", "") + "  " + r.recognize(VALUEMAP));
                }
            } catch (IOException e) {
                System.out.println("Wrong File Format "+file.getName());

                counter= file.getName().equals("error.txt") ? counter+1 : counter;
            }
        }
        System.out.println(counter + " tests passed!");
        System.out.println(testDir.listFiles().length - counter + "   tests failed");
        System.out.println("Result is " + (counter / testDir.listFiles().length) * 100 + "%");
        assertTrue((counter / testDir.listFiles().length) * 100 > 97);

    }

    @Test
    void negativeTest() {
        StringBuilder results = new StringBuilder();
        int counter = 0;
        for (File file : negTestDir.listFiles()
        ) {
            try {
                BufferedImage img = ImageIO.read(file);
                Recognizer r = new Recognizer(img);
                if (file.getName().replaceFirst("[.][^.]+$", "").equals(r.recognize(VALUEMAP)) && !file.getName().equals("error.txt")) {
                    System.out.println("Passed  " + file.getName() + "  " + r.recognize(VALUEMAP));

                } else {
                    System.out.println("Not Passed" + file.getName().replaceFirst("[.][^.]+$", "") + "  " + r.recognize(VALUEMAP));
                    counter++;
                }
            } catch (IOException e) {
                System.out.println("Wrong File Format "+file.getName());
            }
        }

        assertTrue(counter == 2);

    }

}