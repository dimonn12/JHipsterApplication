package com.jhipster.application.config;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jhipster.application.domain.util.CustomDateTimeDeserializer;
import com.jhipster.application.domain.util.CustomDateTimeSerializer;
import com.jhipster.application.domain.util.CustomLocalDateSerializer;
import com.jhipster.application.domain.util.ISO8601LocalDateDeserializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public JodaModule jacksonJodaModule() {
        JodaModule module = new JodaModule();
        module.addSerializer(DateTime.class, new CustomDateTimeSerializer());
        module.addDeserializer(DateTime.class, new CustomDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        module.addDeserializer(LocalDate.class, new ISO8601LocalDateDeserializer());
        return module;
    }
}
