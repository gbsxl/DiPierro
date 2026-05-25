package Di.Pierro.domain.model;

import java.util.UUID;

public class Business {
    UUID id;
    String legalName;
    String cnpj;
    String fantasyName;
    String phoneNumber;
    String email;
    boolean isPublicCompany;
    Actor actor;

    public Business(String legalName, String cnpj, String fantasyName, String phoneNumber, String email, boolean isPublicCompany) {
        this.id = UUID.randomUUID();
        this.legalName = legalName;
        this.cnpj = cnpj;
        this.fantasyName = fantasyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPublicCompany = isPublicCompany;
        this.actor = new Actor();
    }

    public Business(UUID id, String legalName, String cnpj, String fantasyName, String phoneNumber, String email, boolean isPublicCompany, Actor actor) {
        this.id = id;
        this.legalName = legalName;
        this.cnpj = cnpj;
        this.fantasyName = fantasyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPublicCompany = isPublicCompany;
        this.actor = actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPublicCompany() {
        return isPublicCompany;
    }

    public Actor getActor() {
        return actor;
    }
}
