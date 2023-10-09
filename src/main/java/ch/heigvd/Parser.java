package ch.heigvd;

import picocli.CommandLine.*;

import java.io.*;
import java.nio.charset.Charset;

@Command(name = "extractmd", description = "Extracts metadata information from image files.")
public class Parser implements Runnable {
    @Parameters(index = "0", description = "The directory containing the image files to parse.")
    private File directory;

    @Option(names = {"-v", "--verbose"})
    private boolean verbose;

    @Option(names = {"-o", "--output-file"})
    private String outputFilename;

    @Option(names = {"-c", "--charset"}, defaultValue = "UTF-8")
    private String charset;

    @Option(names = {"-f", "--file-format"}, defaultValue = "txt")
    private String fileFormat;

    @Override
    public void run() {
        if (!directory.exists()) {
            System.err.printf("Directory %s does not exist.%n", directory);
            return;
        }

        if (!(fileFormat.equals("txt") || fileFormat.equals("csv"))) {
            System.err.printf("File format %s not supported.%n", fileFormat);
            return;
        }

        // A lot of string concatenation is going to take place inside the for loop so a StringBuilder object will be more performant.
        StringBuilder output = new StringBuilder();

        // We could pass a filter object to listFiles() to get only image files, but I couldn't figure that out yet.
        File[] files = directory.listFiles();

        if (fileFormat.equals("csv")) {
            output.append("filename,date,camera,manufacturer,gps_latitude,gps_longitude\n");
        }

        for (int progress = 0; progress < files.length; progress++) {
            File file = files[progress];

            // Is image file
            if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) {
                javaxt.io.Image image = new javaxt.io.Image(file.getAbsolutePath());
                java.util.HashMap<Integer, Object> exif = image.getExifTags();

                if (fileFormat.equals("txt")) {
                    output.append("Filename: ").append(file.getName()).append('\n');
                    output.append("Date: ").append(exif.get(0x0132)).append('\n');
                    output.append("Camera: ").append(exif.get(0x0110)).append('\n');
                    output.append("Manufacturer: ").append(exif.get(0x010F)).append('\n');

                    double[] gps = image.getGPSCoordinate();
                    if (gps != null) {
                        output.append("GPS Coordinate: ").append(gps[0]).append(", ").append(gps[1]).append('\n');
                    }

                } else if (fileFormat.equals("csv")) {
                    output.append(file.getName()).append(',')
                            .append(exif.get(0x0132)).append(',')
                            .append(exif.get(0x0110)).append(',')
                            .append(exif.get(0x010F));

                    double[] gps = image.getGPSCoordinate();
                    if (gps != null) {
                        output.append(',').append(gps[0]).append(",").append(gps[1]);
                    }
                }

                // If not last element.
                if (progress < files.length - 1) {
                    output.append('\n');
                }

                printVerbose(String.format("File %s processed. (%d/%d)", file.getName(), progress + 1, files.length));
            }

        }

        // Decide what to do with the output depending on whether the user provided an output filename.
        if (outputFilename == null) {
            System.out.println(output);
        } else {
            printVerbose("\nOutput: ");
            printVerbose(output.toString());

            try {
                saveToFile(output, charset);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }

    }

    private void saveToFile(StringBuilder output, String charset) throws IOException {
        FileWriter writer = new FileWriter(outputFilename, Charset.forName(charset));
        writer.write(output.toString());
        writer.close();

        System.out.println();
        System.out.printf("File %s generated successfully.%n", outputFilename);
    }

    private void printVerbose(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
}
