package Di.Pierro.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class ActorIndicator {
    private UUID id;
    private String indicatorType;
    private String value;
    private String source;
    private LocalDate date;
    private Actor actor;

    public static ActorIndicator createActorIndicator(
            String indicatorType,
            String value,
            String source,
            LocalDate date,
            Actor actor
    ) {
        return new ActorIndicator(
                UUID.randomUUID(),
                indicatorType,
                value,
                source,
                date,
                actor
        );
    }

    public ActorIndicator(UUID id, String indicatorType, String value, String source, LocalDate date, Actor actor) {
        this.id = id;
        this.indicatorType = indicatorType;
        this.value = value;
        this.source = source;
        this.date = date;
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        this.indicatorType = indicatorType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
