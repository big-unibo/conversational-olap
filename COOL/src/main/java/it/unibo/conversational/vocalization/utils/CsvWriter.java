package it.unibo.conversational.vocalization.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CsvWriter {

    private final List<List<String>> lines;
    private final String outputPath;

    public CsvWriter(String outputPath) {
        this.lines = new ArrayList<>();
        this.outputPath = outputPath;
    }

    public void registerLine(List<Object> line) {
        this.lines.add(line.stream().map(Objects::toString).collect(Collectors.toList()));
    }

    public void writeLines() throws FileNotFoundException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        File outputDirectory = new File(this.outputPath);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        File outputFile = new File(this.outputPath + "/Volap " + dtf.format(LocalDateTime.now()) + ".csv");
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            this.lines.stream().map(this::convertToCsv).forEach(printWriter::println);
            this.lines.clear();
        }
    }

    private String convertToCsv(List<String> data) {
        return data.stream().map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}
