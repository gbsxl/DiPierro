package Di.Pierro.domain.model;

import Di.Pierro.domain.enums.State;
import Di.Pierro.infrastructure.exception.custom.UnprocessableEntityException;

import java.util.UUID;

public class Address {
    private UUID id;
    private String postalCode;
    private String streetAddress;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private State state;
    private Actor actor;

    public Address() {
    }

    public Address(String postalCode, String streetAddress, String number, String complement, String neighborhood, String city, String state) {
        this(UUID.randomUUID(), postalCode, streetAddress, number, complement, neighborhood, city, State.fromSigla(state), null);
    }

    public Address(String postalCode, String streetAddress, String number, String complement, String neighborhood, String city, State state) {
        this(UUID.randomUUID(), postalCode, streetAddress, number, complement, neighborhood, city, state, null);
    }

    public Address(UUID id, String postalCode, String streetAddress, String number, String complement, String neighborhood, String city, State state, Actor actor) {
        this.id = id != null ? id : UUID.randomUUID();
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.actor = actor;
    }

    public static void validateEntityAssociation(Object entityOwner) {
        if (entityOwner != null && !(entityOwner instanceof Person) && !(entityOwner instanceof Business)) {
            throw new UnprocessableEntityException("address.actor-not-allowed", "Address can only be associated with Person or Business actors.");
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
