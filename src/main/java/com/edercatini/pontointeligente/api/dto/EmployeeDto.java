package com.edercatini.pontointeligente.api.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class EmployeeDto {

    private Long id;
    private String name;
    private String mail;
    private Optional<String> password = Optional.empty();
    private Optional<Object> hourValue = Optional.empty();
    private Optional<String> amountOfWorkTime = Optional.empty();
    private Optional<String> amountOfLunchTime = Optional.empty();

    public EmployeeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "Nome não pode ser vazio.")
    @Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Email não pode ser vazio.")
    @Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
    @Email(message="Email inválido.")
    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Optional<String> getPassword() {
        return this.password;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    public Optional<Object> getHourValue() {
        return this.hourValue;
    }

    public void setHourValue(Optional<Object> hourValue) {
        this.hourValue = hourValue;
    }

    public Optional<String> getAmountOfWorkTime() {
        return this.amountOfWorkTime;
    }

    public void setAmountOfWorkTime(Optional<String> amountOfWorkTime) {
        this.amountOfWorkTime = amountOfWorkTime;
    }

    public Optional<String> getAmountOfLunchTime() {
        return this.amountOfLunchTime;
    }

    public void setAmountOfLunchTime(Optional<String> amountOfLunchTime) {
        this.amountOfLunchTime = amountOfLunchTime;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", password=" + password +
                ", hourValue=" + hourValue +
                ", amountOfWorkTime=" + amountOfWorkTime +
                ", amountOfLunchTime=" + amountOfLunchTime +
                '}';
    }
}