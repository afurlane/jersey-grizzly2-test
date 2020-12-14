package org.example.infrastructure;

public class EndpointLogLine {

    private static final String DEFAULT_FORMAT = "   %-7s %s";
    final String httpMethod;
    final String path;
    final String format;

    public EndpointLogLine(String httpMethod, String path, String format) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.format = format == null ? DEFAULT_FORMAT : format;
    }

    @Override
    public String toString() {
        return String.format(format, httpMethod, path);
    }
}
