package com.edercatini.pontointeligente.api.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "enterprise")
public class Enterprise implements Serializable {
    private static final long serialVersionUID = 5880330059053379870L;

    private Long id;
    private String socialName;
    private String document;
    private Date registeredIn;
    private Date updatedIn;
    private List<Employee> employees;

    public Enterprise() {
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Column(name="registered_in", nullable=false)
    public Date getRegisteredIn() {
        return registeredIn;
    }

    public void setRegisteredIn(Date registeredIn) {
        this.registeredIn = registeredIn;
    }

    @Column(name="updated_in", nullable=false)
    public Date getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(Date updatedIn) {
        this.updatedIn = updatedIn;
    }

    @OneToMany(mappedBy="enterprise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
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
        return "Enterprise{" + "id=" + id + ", socialName='" + socialName + '\'' + ", document='" + document + '\'' + '}';
    }
}