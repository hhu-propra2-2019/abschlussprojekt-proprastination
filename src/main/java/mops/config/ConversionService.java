package mops.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ConversionService extends FormattingConversionService {

    @Autowired
    private DateFormatter dateFormatter;

    @Override
    public void addFormatter(Formatter<?> formatter) {
        super.addFormatter();
    }

}
