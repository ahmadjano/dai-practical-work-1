# DAI - Practical work 1 - ExtractMD

ExtractMD is a tool that extracts metadata information from image files and saves these data in a form of a text file. Picture files contain EXIF tags that are used largely to encode additional information related to image generation by digital cameras.

The EXIF tags extracted by this tool are the following:

HEX | Tag          | Description
--- |--------------| -----------
`0x0132` | Date         | Timestamp when the picture was taken
`0x0110` | Camera       | Camera model name 
`0x010F` | Manufacturer | Manufacturer name 

Additionally, GPS information is extracted when available.
## Build

This project is written in **Java** and can be build with **Maven**. A Maven wrapper is already include so you could simply run the following command:

`./mvnw dependency:resolve clean compile package`

Alternatively, there is a pre-configuration included to generate the JAR file in case you use the IDE **Intellij IDEA** called `Generate a JAR package file`

## Usage

`java --jar ./package.jar [-v] [-c=<charset>] [-f=<fileFormat>] [-o=<outputFilename>] <directory>`

The only required parameter is the **directory path** containing the images to process.

Execute the following command as a start:

`java -jar target/dai-practical-work-1-1.0-SNAPSHOT.jar /your-pictures-folder`

#### Specify an output file
The output file is going to contain all the metadata extracted from the images. Default file format is a plain text (.txt) file.

`java -jar target/dai-practical-work-1-1.0-SNAPSHOT.jar /your-pictures-folder -o data.txt`

#### Specify a file format for the output

`java -jar target/dai-practical-work-1-1.0-SNAPSHOT.jar /your-pictures-folder -o data.csv --file-format=csv`

#### Specify a charset for the output file

`java -jar target/dai-practical-work-1-1.0-SNAPSHOT.jar /your-pictures-folder -o data.csv --charset=UTF-8 --file-format=csv`

#### Verbose mode
Print the progress and the output data in console using the `-v` or `--verbose` flag.
