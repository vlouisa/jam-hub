package dev.louisa.jam.hub.testsupport;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoggerSpy {

    private final List<ILoggingEvent> events;

    private LoggerSpy(ListAppender<ILoggingEvent> appender) {
        this.events = appender.list;
    }

    public static LoggerSpy forClass(Class<?> clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        return new LoggerSpy(appender);
    }

    public LevelAssert assertThatAtLeastOneMessageWithLevel(ch.qos.logback.classic.Level level) {
        return new LevelAssert(level, events.stream()
                .filter(event -> event.getLevel().equals(level))
                .collect(Collectors.toList()));
    }

    public static class LevelAssert {
        private final Level level;
        private final List<String> allMessages;  // all messages at this level

        public LevelAssert(Level level, List<ILoggingEvent> levelEvents) {
            this.level = level;
            this.allMessages = levelEvents.stream()
                    .map(ILoggingEvent::getFormattedMessage)
                    .toList();
        }

        public void contains(String... fragments) {
            // at least one message should contain *all* fragments
            List<String> matchingMessages = allMessages.stream()
                    .filter(msg -> Arrays.stream(fragments).allMatch(msg::contains))
                    .toList();

            SoftAssertions softly = new SoftAssertions();

            softly.assertThat(matchingMessages)
                    .overridingErrorMessage(buildErrorMessage(fragments))
                    .isNotEmpty();

            softly.assertAll();
        }

        private String buildErrorMessage(String[] fragments) {
            String expectedJoined = Arrays.stream(fragments)
                    .map(s -> " - '" + s + "'")
                    .collect(Collectors.joining("\n"));

            String actualJoined = allMessages.isEmpty()
                    ? " <no messages logged at this level>"
                    : allMessages.stream().map(m -> " - " + m).collect(Collectors.joining("\n"));

            return String.format(
                    "Expected to find a single log message (level %s) containing all of:\n%s\n\nbut found these messages:\n%s",
                    level, expectedJoined, actualJoined
            );
        }
    }}