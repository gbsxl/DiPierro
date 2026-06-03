package Di.Pierro.domain.model;

import Di.Pierro.domain.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private BigDecimal value;
    private Currency currency;
    private OffsetDateTime transactionDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Actor actorSender;
    private Actor actorReceiver;

    public static Transaction createTransaction(BigDecimal value, Currency currency, OffsetDateTime transactionDate){
        return new Transaction(
                UUID.randomUUID(),
                value,
                currency,
                transactionDate,
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                new Actor(),
                new Actor()
        );
    }

    public Transaction(UUID id, BigDecimal value, Currency currency, OffsetDateTime transactionDate, OffsetDateTime createdAt, OffsetDateTime updatedAt, Actor actorSender, Actor actorReceiver) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.actorSender = actorSender;
        this.actorReceiver = actorReceiver;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public OffsetDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(OffsetDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Actor getActorSender() {
        return actorSender;
    }

    public void setActorSender(Actor actorSender) {
        this.actorSender = actorSender;
    }

    public Actor getActorReceiver() {
        return actorReceiver;
    }

    public void setActorReceiver(Actor actorReceiver) {
        this.actorReceiver = actorReceiver;
    }
}
