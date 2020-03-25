package mops.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.standard.InstantFormatter;

import java.text.ParseException;
import java.time.Instant;
import java.util.Locale;

@Bean
public class DateFormatter implements Formatter<Instant> {

    @Autowired
    MessageSource messageSource;

    public DateFormatter() {
        super();
    }

    public Instant parse(final String text, final Locale locale) throws ParseException {
        final InstantFormatter dateFormatter = new InstantFormatter();
        return dateFormatter.parse(text, locale);
    }

    public String print(final Instant object, final Locale locale) {
        final InstantFormatter instantFormatter = new InstantFormatter();
        return instantFormatter.print(object, locale);
    }

}
