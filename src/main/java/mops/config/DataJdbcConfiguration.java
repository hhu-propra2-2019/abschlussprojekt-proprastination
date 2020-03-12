package mops.config;

import org.postgresql.util.PGobject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.sql.SQLException;
import java.util.Arrays;

@EnableAutoConfiguration
@Configuration
public class DataJdbcConfiguration extends AbstractJdbcConfiguration {
    /**
     * JDBC Configuration to Convert String to PGobject and vise versa without Postgres cast.
     *
     * @return .
     */

    @Bean
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(
                Arrays.asList(PGobjectToStringConverter.INSTANCE, StringToPGobjectConverter.INSTANCE));

    }

    //@Bean
    //QueryMappingConfiguration rowMappers() {
    //    return new DefaultQueryMappingConfiguration().registerRowMapper(ApplicantDTO.class, new ApplicantMapper());
    //}

    @ReadingConverter
    enum PGobjectToStringConverter implements Converter<PGobject, String> {

        INSTANCE;

        @Override
        public String convert(final PGobject source) {
            String returnValue;
            if (!source.getType().equals("json")) {
                return null;
            }
            returnValue = source.getValue();
            return returnValue;
        }
    }

    @WritingConverter
    enum StringToPGobjectConverter implements Converter<String, PGobject> {
        INSTANCE;

        @Override
        public PGobject convert(final String source) {
            PGobject pgobject = new PGobject();
            pgobject.setType("json");
            try {
                pgobject.setValue(source);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return pgobject;
        }
    }

}
