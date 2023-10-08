package ch.heigvd;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Parser()).execute(args);
        System.exit(exitCode);
    }
}