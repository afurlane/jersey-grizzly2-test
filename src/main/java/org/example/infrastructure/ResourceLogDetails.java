package org.example.infrastructure;

import org.apache.logging.log4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Default
public class ResourceLogDetails {

    @Inject
    private Logger logger;

    private final Comparator<EndpointLogLine> COMPARATOR
            = Comparator.comparing((EndpointLogLine e) -> e.path)
            .thenComparing((EndpointLogLine e) -> e.httpMethod);

    private Set<EndpointLogLine> logLines = new TreeSet<>(COMPARATOR);

    public void log() {
        StringBuilder sb = new StringBuilder("\nAll endpoints for Jersey application\n");
        logLines.stream().forEach((line) -> {
            sb.append(line).append("\n");
        });
        logger.info(sb.toString());
    }

    public void clearEndpointLogLines() {
        this.logLines.clear();
    }

    public void addEndpointLogLines(Set<EndpointLogLine> logLines) {
        this.logLines.addAll(logLines);
    }
}
