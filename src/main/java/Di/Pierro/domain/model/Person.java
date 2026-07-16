package Di.Pierro.domain.model;
import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public class Person {
    private UUID id;
    private String completeName;
    private String cpf;
    private String address;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Actor actor;

    public Person() {
    }

    public Person(String completeName, String cpf, String address, Gender gender, String phoneNumber, String email, Actor actor) {
        this.id = UUID.randomUUID();
        this.completeName = completeName;
        this.cpf = cpf;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.actor = actor;
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

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
