package ch.heigvd;

import picocli.CommandLine.*;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "extractmd", description = "Extracts metadata information from image files.")
public class Parser implements Runnable {
    @Parameters(index = "0", description = "The directory containing the image files to parse.")
    private File directory;


    @Override
    public void run() {
        if (!directory.exists()) {
            System.err.printf("Directory %s does not exist.%n", directory);
            return;
        }

        // We could pass a filter object to listFiles() to get only image files.
        for (final File file : directory.listFiles()) {
            // Is image file
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                javaxt.io.Image image = new javaxt.io.Image(file.getAbsolutePath());
                java.util.HashMap<Integer, Object> exif = image.getExifTags();

                System.out.println("Filename: " + file.getName());
                System.out.println("Date: " + exif.get(0x0132));
                System.out.println("Camera: " + exif.get(0x0110));
                System.out.println("Manufacturer: " + exif.get(0x010F));

                double[] coord = image.getGPSCoordinate();
                if (coord != null) {
                    System.out.println("GPS Coordinate: " + coord[0] + ", " + coord[1]);
                    System.out.println("GPS Datum: " + image.getGPSDatum());
                }

                System.out.println();
            }
        }
    }
}
