import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
    public static String readFile(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        StringBuilder reading = new StringBuilder();
        while (sc.hasNextLine()) {
            reading.append(sc.nextLine());
            reading.append("\n");
        }
        return reading.toString();
    }
}
