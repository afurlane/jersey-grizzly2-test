package org.example.infrastructure;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.server.model.ResourceModel;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

public class EndpointLoggingListener implements ApplicationEventListener {

    @Context
    ServletContext httpServletContext;

    @Inject
    ResourceLogDetails resourceLogDetails;

    private static final TypeResolver TYPE_RESOLVER = new TypeResolver();

    private String applicationPath = null;
    private boolean withOptions = false;
    private boolean withWadl = false;

    @Override
    public void onEvent(ApplicationEvent event) {
        if (event.getType() == ApplicationEvent.Type.INITIALIZATION_APP_FINISHED) {
            final ResourceModel resourceModel = event.getResourceModel();
            resourceLogDetails.clearEndpointLogLines();
            resourceModel.getResources().stream().forEach((resource) -> {
                resourceLogDetails.addEndpointLogLines(getLinesFromResource(resource));
            });
            resourceLogDetails.log();
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }

    public EndpointLoggingListener withOptions() {
        this.withOptions = true;
        return this;
    }

    public EndpointLoggingListener withWadl() {
        this.withWadl = true;
        return this;
    }

    private Set<EndpointLogLine> getLinesFromResource(Resource resource) {
        Set<EndpointLogLine> logLines = new HashSet<>();
        populate(this.applicationPath, false, resource, logLines);
        return logLines;
    }

    private void populate(String basePath, Class<?> klass, boolean isLocator,
                          Set<EndpointLogLine> endpointLogLines) {
        populate(basePath, isLocator, Resource.from(klass), endpointLogLines);
    }

    private void populate(String basePath, boolean isLocator, Resource resource,
                          Set<EndpointLogLine> endpointLogLines) {
        if (!isLocator) {
            basePath = normalizePath(basePath, resource.getPath());
        }

        for (ResourceMethod method : resource.getResourceMethods()) {
            if (!withOptions && method.getHttpMethod().equalsIgnoreCase("OPTIONS")) {
                continue;
            }
            if (!withWadl && basePath.contains(".wadl")) {
                continue;
            }
            endpointLogLines.add(new EndpointLogLine(method.getHttpMethod(), basePath, null));
        }

        for (Resource childResource : resource.getChildResources()) {
            for (ResourceMethod method : childResource.getAllMethods()) {
                if (method.getType() == ResourceMethod.JaxrsType.RESOURCE_METHOD) {
                    final String path = normalizePath(basePath, childResource.getPath());
                    if (!withOptions && method.getHttpMethod().equalsIgnoreCase("OPTIONS")) {
                        continue;
                    }
                    if (!withWadl && path.contains(".wadl")) {
                        continue;
                    }
                    endpointLogLines.add(new EndpointLogLine(method.getHttpMethod(), path, null));
                } else if (method.getType() == ResourceMethod.JaxrsType.SUB_RESOURCE_LOCATOR) {
                    final String path = normalizePath(basePath, childResource.getPath());
                    final ResolvedType responseType = TYPE_RESOLVER
                            .resolve(method.getInvocable().getResponseType());
                    final Class<?> erasedType = !responseType.getTypeBindings().isEmpty()
                            ? responseType.getTypeBindings().getBoundType(0).getErasedType()
                            : responseType.getErasedType();
                    populate(path, erasedType, true, endpointLogLines);
                }
            }
        }
    }

    private static String normalizePath(String basePath, String path) {
        if (path == null) {
            return basePath;
        }
        if (basePath.endsWith("/")) {
            return path.startsWith("/") ? basePath + path.substring(1) : basePath + path;
        }
        return path.startsWith("/") ? basePath + path : basePath + "/" + path;
    }

    @PostConstruct
    public void getApplicationBaseUrl () {
        /* this.applicationPath = String.format("%s://%s:%s%s",
                httpServletContext.getScheme(), httpServletContext.getServerName(),
                httpServletContext.getServerPort(), httpServletContext.getContextPath());

         */
        this.applicationPath = String.format(httpServletContext.getRealPath(httpServletContext.getContextPath()));
    }
}
