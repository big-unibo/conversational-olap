package it.unibo.conversational.vocalization.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvWriter {

    private final List<List<String>> registeredLines;

    public CsvWriter() {
        this.registeredLines = new ArrayList<>();
    }

    public void registerLine(String query, String error1, String quality1, String error2, String quality2, String error3, String quality3) {
        this.registeredLines.add(Arrays.asList(query, error1, quality1, error2, quality2, error3, quality3));
    }

    public void writeRegisteredLines() throws FileNotFoundException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        File outputFile = new File("outputs/Session " + dtf.format(LocalDateTime.now()) + ".csv");
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            this.registeredLines.stream().map(this::convertToCsv).forEach(printWriter::println);
            this.registeredLines.clear();
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
