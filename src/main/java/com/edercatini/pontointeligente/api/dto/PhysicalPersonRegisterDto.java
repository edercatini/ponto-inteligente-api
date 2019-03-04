package com.edercatini.pontointeligente.api.dto;

import java.util.Optional;

public class PhysicalPersonRegisterDto {

    private Long id;
    private String name;
    private String mail;
    private String password;
    private String cpf;
    private Optional<String> hourValue = Optional.empty();
    private Optional<String> amountOfWorkingHours = Optional.empty();
    private Optional<String> amountOfLunchHours = Optional.empty();
    private String cnpj;

    public PhysicalPersonRegisterDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Optional<String> getHourValue() {
        return hourValue;
    }

    public void setHourValue(Optional<String> hourValue) {
        this.hourValue = hourValue;
    }

    public Optional<String> getAmountOfWorkingHours() {
        return amountOfWorkingHours;
    }

    public void setAmountOfWorkingHours(Optional<String> amountOfWorkingHours) {
        this.amountOfWorkingHours = amountOfWorkingHours;
    }

    public Optional<String> getAmountOfLunchHours() {
        return amountOfLunchHours;
    }

    public void setAmountOfLunchHours(Optional<String> amountOfLunchHours) {
        this.amountOfLunchHours = amountOfLunchHours;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "PhysicalPersonRegisterDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", cpf='" + cpf + '\'' +
                ", hourValue=" + hourValue +
                ", amountOfWorkingHours=" + amountOfWorkingHours +
                ", amountOfLunchHours=" + amountOfLunchHours +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
