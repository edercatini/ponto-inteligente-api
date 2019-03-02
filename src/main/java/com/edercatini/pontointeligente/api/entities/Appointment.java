package com.edercatini.pontointeligente.api.entities;

import com.edercatini.pontointeligente.api.enums.Appointments;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {
    private static final long serialVersionUID = 199263331399657757L;

    private Long id;
    private Date date;
    private String description;
    private String location;
    private Date registeredIn;
    private Date updatedIn;
    private Appointments type;
    private Employee employee;

    public Appointment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    public Appointments getType() {
        return type;
    }

    public void setType(Appointments type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        return "Appointment{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", registeredIn=" + registeredIn +
                ", updatedIn=" + updatedIn +
                '}';
    }
}