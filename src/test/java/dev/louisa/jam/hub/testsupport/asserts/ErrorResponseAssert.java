package dev.louisa.jam.hub.testsupport.asserts;

import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import org.assertj.core.api.AbstractAssert;

import java.util.List;

public class ErrorResponseAssert extends AbstractAssert<ErrorResponseAssert, ErrorResponse> {

    public ErrorResponseAssert(ErrorResponse actual) {
        super(actual, ErrorResponseAssert.class);
    }

    public static ErrorResponseAssert assertThatErrorResponse(ErrorResponse actual) {
        return new ErrorResponseAssert(actual);
    }

    public ErrorResponseAssert hasErrorCode(String expected) {
        isNotNull();
        if (!expected.equals(actual.errorCode())) {
            failWithMessage("Expected errorCode to be <%s> but was <%s>", expected, actual.errorCode());
        }
        return this;
    }

    public ErrorResponseAssert hasMessage(String expected) {
        isNotNull();
        if (!expected.equals(actual.message())) {
            failWithMessage("Expected message to be <%s> but was <%s>", expected, actual.message());
        }
        return this;
    }

    public ErrorResponseAssert hasContext(List<String> expected) {
        isNotNull();
        if (!expected.equals(actual.context())) {
            failWithMessage("Expected context to be <%s> but was <%s>", expected, actual.context());
        }
        return this;
    }

    public ErrorResponseAssert hasEmptyContext() {
        isNotNull();
        if (actual.context() == null || !actual.context().isEmpty()) {
            failWithMessage("Expected context to be empty but was <%s>", actual.context());
        }
        return this;
    }
}