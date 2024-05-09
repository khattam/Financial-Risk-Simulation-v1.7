package banking;

import java.io.FileWriter;
import java.io.IOException;

public class SimpleFileWriteTest {
    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("C:\\Users\\khattam\\OneDrive - Rose-Hulman Institute of Technology\\TestFile.csv")) {
            writer.write("Test data");
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}

