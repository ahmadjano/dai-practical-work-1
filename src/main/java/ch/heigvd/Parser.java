package ch.heigvd;

import picocli.CommandLine.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

        StringBuilder output = new StringBuilder();

        // We could pass a filter object to listFiles() to get only image files.
        for (final File file : directory.listFiles()) {
            // Is image file
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                javaxt.io.Image image = new javaxt.io.Image(file.getAbsolutePath());
                java.util.HashMap<Integer, Object> exif = image.getExifTags();

                output.append("Filename: ").append(file.getName()).append('\n');;
                output.append("Date: ").append(exif.get(0x0132)).append('\n');
                output.append("Camera: ").append(exif.get(0x0110)).append('\n');;
                output.append("Manufacturer: ").append(exif.get(0x010F)).append('\n');;

                double[] coord = image.getGPSCoordinate();
                if (coord != null) {
                    output.append("GPS Coordinate: ").append(coord[0]).append(", ").append(coord[1]).append('\n');
                }

                output.append('\n');
            }
        }

        System.out.println(output);
        try {
            saveToFile(output);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void saveToFile(StringBuilder output) throws IOException {
        FileWriter writer = new FileWriter("metadata.txt", StandardCharsets.UTF_8);
        writer.write(output.toString());
        writer.close();
    }
}
