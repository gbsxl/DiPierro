package Di.Pierro.domain.model;
import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public class Person {
    private UUID id;
    private String completeName;
    private String cpf;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Actor actor;


    public Person(String completeName, String cpf, Gender gender, String phoneNumber, String email) {
        this.id = UUID.randomUUID();
        this.completeName = completeName;
        this.cpf = cpf;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.actor = new Actor();
    }

    public Person(UUID id, String completeName, String cpf, Gender gender, String phoneNumber, String email, Actor actor) {
        this.id = id;
        this.completeName = completeName;
        this.cpf = cpf;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.actor = actor;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public String getCompleteName() {
        return completeName;
    }

    public String getCpf() {
        return cpf;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Actor getActor() {
        return actor;
    }
}
