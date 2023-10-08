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

This project is written in Java and can be build using Maven.

`./mvnw dependency:resolve clean compile package`

Alternatively, there is a build pre-configuration included to generate the JAR file in case you use Intellij IDEA called `Generate a JAR package file`

## Usage

Execute the following command:

`java -jar target/dai-practical-work-1-1.0-SNAPSHOT.jar /your-pictures-folder`