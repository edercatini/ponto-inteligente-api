package com.edercatini.pontointeligente.api.entities;

import com.edercatini.pontointeligente.api.enums.Profiles;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1409560523034768361L;

    private Long id;
    private String name;
    private String mail;
    private String password;
    private String document;
    private BigDecimal hourValue;
    private Long amountOfWorkTime;
    private Long amountOfLunchTime;
    private Profiles profile;
    private Date registeredIn;
    private Date updatedIn;
    private Enterprise enterprise;
    private List<Appointment> appointments;

    public Employee() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Column(name = "hour_value", nullable = true)
    public BigDecimal getHourValue() {
        return hourValue;
    }

    @Transient
    public Optional<BigDecimal> getHourValueOpt() {
        return Optional.ofNullable(hourValue);
    }

    public void setHourValue(BigDecimal hourValue) {
        this.hourValue = hourValue;
    }

    @Column(name = "amount_of_worktime", nullable = true)
    public Long getAmountOfWorkTime() {
        return amountOfWorkTime;
    }

    @Transient
    public Optional<Long> getAmountOfWorkTimeOpt() {
        return Optional.ofNullable(amountOfWorkTime);
    }

    public void setAmountOfWorkTime(Long amountOfWorkTime) {
        this.amountOfWorkTime = amountOfWorkTime;
    }

    @Column(name = "amount_of_lunchtime", nullable = true)
    public Long getAmountOfLunchTime() {
        return amountOfLunchTime;
    }

    @Transient
    public Optional<Long> getAmountOfLunchTimeOpt() {
        return Optional.ofNullable(amountOfLunchTime);
    }

    public void setAmountOfLunchTime(Long amountOfLunchTime) {
        this.amountOfLunchTime = amountOfLunchTime;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "profile", nullable = false)
    public Profiles getProfile() {
        return profile;
    }

    public void setProfile(Profiles profile) {
        this.profile = profile;
    }

    @Column(name = "registered_in", nullable = false)
    public Date getRegisteredIn() {
        return registeredIn;
    }

    public void setRegisteredIn(Date registeredIn) {
        this.registeredIn = registeredIn;
    }

    @Column(name = "updated_in", nullable = false)
    public Date getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(Date updatedIn) {
        this.updatedIn = updatedIn;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedIn = new Date();
    }

    @PrePersist
    public void prePersist() {
        final Date date = new Date();
        this.registeredIn = date;
        this.updatedIn = date;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", document='" + document + '\'' +
                ", hourValue=" + hourValue +
                ", amountOfWorkTime=" + amountOfWorkTime +
                ", amountOfLunchTime=" + amountOfLunchTime +
                '}';
    }
}