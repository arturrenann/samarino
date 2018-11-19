package br.com.samarino.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Hotel.
 */
@Entity
@Table(name = "hotel")
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "cep")
    private String cep;

    @Column(name = "qtd_acomodacoes")
    private Integer qtdAcomodacoes;

    @Column(name = "valor_diaria")
    private Integer valorDiaria;

    @ManyToMany(mappedBy = "hoteis")
    @JsonIgnore
    private Set<Reservar> reservars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Hotel nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public Hotel localizacao(String localizacao) {
        this.localizacao = localizacao;
        return this;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getCep() {
        return cep;
    }

    public Hotel cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getQtdAcomodacoes() {
        return qtdAcomodacoes;
    }

    public Hotel qtdAcomodacoes(Integer qtdAcomodacoes) {
        this.qtdAcomodacoes = qtdAcomodacoes;
        return this;
    }

    public void setQtdAcomodacoes(Integer qtdAcomodacoes) {
        this.qtdAcomodacoes = qtdAcomodacoes;
    }

    public Integer getValorDiaria() {
        return valorDiaria;
    }

    public Hotel valorDiaria(Integer valorDiaria) {
        this.valorDiaria = valorDiaria;
        return this;
    }

    public void setValorDiaria(Integer valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public Set<Reservar> getReservars() {
        return reservars;
    }

    public Hotel reservars(Set<Reservar> reservars) {
        this.reservars = reservars;
        return this;
    }

    public Hotel addReservar(Reservar reservar) {
        this.reservars.add(reservar);
        reservar.getHoteis().add(this);
        return this;
    }

    public Hotel removeReservar(Reservar reservar) {
        this.reservars.remove(reservar);
        reservar.getHoteis().remove(this);
        return this;
    }

    public void setReservars(Set<Reservar> reservars) {
        this.reservars = reservars;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hotel hotel = (Hotel) o;
        if (hotel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hotel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hotel{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", localizacao='" + getLocalizacao() + "'" +
            ", cep='" + getCep() + "'" +
            ", qtdAcomodacoes=" + getQtdAcomodacoes() +
            ", valorDiaria=" + getValorDiaria() +
            "}";
    }
}
