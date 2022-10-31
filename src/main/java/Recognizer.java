import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Map;

public class Recognizer {

    BufferedImage img;
    //    private static final Integer[] Y = {589, 617};
//    private static final Integer[] X = {149, 219, 289, 364, 439};
    private static final Integer[] Y = {589, 617};
    private static final Integer[] X = {149, 219, 289, 364, 439};
    //    private static final Integer[] SIZES = {27, 28};
    public static final Integer[] SIZESRANK = {31, 31};
    public static final Integer[] SIZESSUIT = {23, 21};

    public Recognizer(BufferedImage img) {
        this.img = img;
    } //Утилити объект (принимает картинку)

    public int levenshtein(String source, String target, int threshold) { //функция расстояния Левенштейна(взял из другого проекта, работает и ладно)
        int N1 = source.length();
        int N2 = target.length();
        int p[] = new int[N1 + 1];
        int d[] = new int[N1 + 1];
        int temp[];
        final int boundary = Math.min(N1, threshold) + 1;
        for (int i = 0; i < boundary; i++) p[i] = i;

        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);

        for (int j = 1; j <= N2; j++) {
            char t_j = target.charAt(j - 1);
            d[0] = j;
            int min = Math.max(1, j - threshold);
            int max;
            if (j > Integer.MAX_VALUE - threshold) max = N1;
            else max = Math.min(N1, j + threshold);
            if (min > max) return -1;
            if (min > 1) d[min - 1] = Integer.MAX_VALUE;
            for (int i = min; i <= max; i++) {
                if (source.charAt(i - 1) == t_j) d[i] = p[i - 1];
                else
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]); //1 + минимум от ячейки слева, вверх, по диагонали влево и вверх
            }
            temp = p;
            p = d;
            d = temp;
        }
        if (p[N1] <= threshold) {
            return p[N1];
        }
        return -1;
    }

    private String getBinaryString(int xCoordinate, int yCoordinate, int width, int height) throws IOException { // Делаем из картинки строку
        StringBuilder binaryString = new StringBuilder();
        try {
            BufferedImage symbol = img.getSubimage(xCoordinate, yCoordinate, width, height);
            for (short y = 1; y < height; y++) {
                for (short x = 1; x < width; x++) {

                    int rgb = symbol.getRGB(x, y);
                    binaryString.append((rgb == -8882056) || (rgb == -1) ? " " : "*");// Здесь хэндлим кейс с затемнённой картой.
                }
                binaryString.append("\n"); // для наглядности, будет работать и без этого
            }
        } catch (NullPointerException e) { // да да, я ловлю здесь нпе, но это самы вменяемый способ хэндлить неправильные файлы. Альтернатива писать целый метод валидации для png мне совсем не понравилась.
            throw new IOException();
        }
        return binaryString.toString();
    }

    public String recognize(Map<String, String> patternSet) throws IOException { //Сообственно распознователь. Принимает десериализованную мапу со значениями для сравнения
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) { // Исходим из того, что у нас 5 карт и 10 символов.
            String binaryString = getBinaryString(X[i / 2], i % 2 == 0 ? Y[0] : Y[1], i % 2 == 0 ? SIZESRANK[0] : SIZESSUIT[0], i % 2 == 0 ? SIZESRANK[0] : SIZESSUIT[0]);
            int min = 10000;
            String targetValue = null;
            for (int j = 0; j < 1; j++) {
                for (Map.Entry<String, String> entry : patternSet.entrySet()) {
                    int levenshtein = levenshtein(binaryString, entry.getValue(), 1000);
                    if (levenshtein < min) {
                        min = levenshtein;
                        targetValue = entry.getKey();
                    }
                }
            }
            sb.append((targetValue != null && targetValue.equals("blank")) ? "" : targetValue); // тут проверяем на пустоту.
        }
        return sb.length() < 6 ? sb.append(sb.length() < 1 ? "Can't see any cards " : "  something wrong with flop").toString() : sb.toString();
    }

    public static Map<String, String> deserializeValueMap(FileInputStream stream) {  //метод для распаковки нашей подготовленой мапы. Его можно было вынести в мэйн, но мне так больше нравится.
        try (ObjectInputStream os = new ObjectInputStream(stream)) {
            return (Map<String, String>) os.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("WARNING! I CAN'T FIND A PATTERN MAP!");
            return null;
        }
    }

    public String invokeGetBinaryString(int xCoordinate, int yCoordinate, int width, int height) throws IOException {
        /*это геттер для визуальных тестов. В работе программы он не учавствует(очень не хотелось делать getBinaryString публичным)*/
        System.out.println(getBinaryString(xCoordinate, yCoordinate, width, height));
        return getBinaryString(xCoordinate, yCoordinate, width, height);
    }
}



