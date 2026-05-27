package Di.Pierro.domain.model;

import java.util.UUID;

public class Business {
    UUID id;
    String legalName;
    String cnpj;
    String fantasyName;
    String phoneNumber;
    String email;
    boolean publicCompany;
    Actor actor;

    public static Business createBusiness(String legalName, String cnpj, String fantasyName, String phoneNumber, String email, boolean publicCompany){
        return new Business(
                UUID.randomUUID(),
                legalName,
                cnpj,
                fantasyName,
                phoneNumber,
                email,
                publicCompany,
                new Actor()
        );
    }


    public Business(UUID id, String legalName, String cnpj, String fantasyName, String phoneNumber, String email, boolean publicCompany, Actor actor) {
        this.id = id;
        this.legalName = legalName;
        this.cnpj = cnpj;
        this.fantasyName = fantasyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.publicCompany = publicCompany;
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
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

    public boolean isPublicCompany() {
        return publicCompany;
    }

    public void setPublicCompany(boolean publicCompany) {
        this.publicCompany = publicCompany;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
