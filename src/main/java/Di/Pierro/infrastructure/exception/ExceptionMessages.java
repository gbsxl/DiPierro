package Di.Pierro.infrastructure.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "dipierro")
public class ExceptionMessages {

    private Map<String, Map<String, String>> exceptions = Collections.emptyMap();

    public void setExceptions(Map<String, Map<String, String>> exceptions) {
        this.exceptions = exceptions;
    }

    public String get(String domain, String key) {
        Map<String, String> domainMessages = exceptions.getOrDefault(domain, Collections.emptyMap());
        return domainMessages.getOrDefault(key, key);
    }

    public String get(String domain, String key, String placeholder, String value) {
        return get(domain, key).replace("{" + placeholder + "}", value);
    }
}
