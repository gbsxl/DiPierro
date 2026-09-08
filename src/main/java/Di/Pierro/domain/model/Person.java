package Di.Pierro.domain.model;

import Di.Pierro.domain.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public class Person {
    private UUID id;
    private String completeName;
    private String cpf;
    private Address address;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Boolean isAlive = true;
    private LocalDate deathDate;
    private Actor actor;

    public Person() {
    }

    public Person(String completeName, String cpf, Address address, Gender gender, String phoneNumber, String email, Actor actor) {
        this(completeName, cpf, address, gender, phoneNumber, email, true, null, actor);
    }

    public Person(String completeName, String cpf, Address address, Gender gender, String phoneNumber, String email, Boolean isAlive, Actor actor) {
        this(completeName, cpf, address, gender, phoneNumber, email, isAlive, null, actor);
    }

    public Person(String completeName, String cpf, Address address, Gender gender, String phoneNumber, String email, Boolean isAlive, LocalDate deathDate, Actor actor) {
        this.id = UUID.randomUUID();
        this.completeName = completeName;
        this.cpf = cpf;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isAlive = isAlive != null ? isAlive : true;
        this.deathDate = deathDate;
        this.actor = actor;
        setAddress(address);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isAlive() {
        return isAlive != null ? isAlive : true;
    }

    public Boolean getIsAlive() {
        return isAlive();
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive != null ? isAlive : true;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        if (this.address != null && actor != null) {
            this.address.setActor(actor);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address != null) {
            Address.validateEntityAssociation(this);
            if (this.actor != null) {
                address.setActor(this.actor);
            }
        }
        this.address = address;
    }
}
