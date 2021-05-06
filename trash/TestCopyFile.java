package trash;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestCopyFile {
    public static void main(String[] args) {
        File source = new File("D:\\testMove.txt");
        File destination = new File("D:\\Document\\destination\\");
        try {
            FileUtils.copyFileToDirectory(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
