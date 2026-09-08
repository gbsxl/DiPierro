package Di.Pierro.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Business {
    private UUID id;
    private String legalName;
    private String cnpj;
    private String fantasyName;
    private String phoneNumber;
    private String email;
    private boolean publicCompany;
    private Address address;
    private BigDecimal estimatedNetWorth;
    private BigDecimal capitalStock;
    private LocalDate openDate;
    private Actor actor;

    public Business() {
    }

    public Business(String legalName, String cnpj, String fantasyName, String phoneNumber, String email, boolean publicCompany, Address address, BigDecimal estimatedNetWorth, BigDecimal capitalStock, LocalDate openDate, Actor actor) {
        this.id = UUID.randomUUID();
        this.legalName = legalName;
        this.cnpj = cnpj;
        this.fantasyName = fantasyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.publicCompany = publicCompany;
        this.estimatedNetWorth = estimatedNetWorth;
        this.capitalStock = capitalStock;
        this.openDate = openDate;
        this.actor = actor;
        setAddress(address);
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

    public BigDecimal getEstimatedNetWorth() {
        return estimatedNetWorth;
    }

    public void setEstimatedNetWorth(BigDecimal estimatedNetWorth) {
        this.estimatedNetWorth = estimatedNetWorth;
    }

    public BigDecimal getCapitalStock() {
        return capitalStock;
    }

    public void setCapitalStock(BigDecimal capitalStock) {
        this.capitalStock = capitalStock;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }
}
