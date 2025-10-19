package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * A fluent, type-safe builder for creating exception responses via handleExceptionInternal().
 */
public class ExceptionHandlerBuilder {

    // You inject or subclass something that exposes handleExceptionInternal()
    public interface ExceptionHandlerDelegate {
        ResponseEntity<Object> handleExceptionInternal(
                Exception ex,
                @Nullable Object body,
                HttpHeaders headers,
                HttpStatusCode statusCode,
                WebRequest request);
    }

    // 🔹 Step 1 — set the Exception to handle
    public static ErrorDetailsStage handle(Exception ex, ExceptionHandlerDelegate delegate) {
        if (ex == null) throw new IllegalArgumentException("Exception must not be null");
        if (delegate == null) throw new IllegalArgumentException("Delegate must not be null");
        return new Stages(ex, delegate);
    }

    // --- Stage Interfaces ---
    public interface ErrorDetailsStage {
        RequestStage errorDetails(JamHubError error, List<Id> contextList);
        RequestStage errorDetails(JamHubError error);
    }

    public interface RequestStage {
        BuildStage request(WebRequest request);
    }


    public interface BuildStage {
        ResponseEntity<Object> toResponseEntity();
    }

    // --- Internal staged implementation ---
    private static class Stages implements ErrorDetailsStage, RequestStage, BuildStage {

        private final Exception exception;
        private final ExceptionHandlerDelegate delegate;
        private final HttpHeaders headers = new HttpHeaders();

        private JamHubError error;
        private List<Id> contextLists;

        private WebRequest request;

        private Stages(Exception ex, ExceptionHandlerDelegate delegate) {
            this.exception = ex;
            this.delegate = delegate;
        }

        @Override
        public RequestStage errorDetails(JamHubError error) {
            return errorDetails(error, List.of());
        }

        @Override
        public RequestStage errorDetails(JamHubError error, List<Id> contextList) {
            if (error == null) throw new IllegalArgumentException("error must not be null");
            this.error = error;
            this.contextLists = contextList;
            return this;
        }

        @Override
        public  BuildStage request(WebRequest request) {
            if (request == null) throw new IllegalArgumentException("WebRequest must not be null");
            this.request = request;
            return this;
        }
        
        @Override
        public ResponseEntity<Object> toResponseEntity() {
            // Delegate to your real handleExceptionInternal implementation
            return delegate.handleExceptionInternal(
                    exception, 
                    constructErrorResponse(error, contextLists), 
                    headers, 
                    error.getHttpStatus(), 
                    request);
        }
        private ErrorResponse constructErrorResponse(JamHubError error, List<Id> context) {
            return ErrorResponse.builder()
                    .errorCode(error.getDomainCode() + "-" + error.getErrorCode())
                    .message(error.getMessage())
                    .context(context.isEmpty() ? List.of() : context.stream().map(Id::toString).toList())
                    .build();
        }
    }
}