package br.com.samarino.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Cliente entity. This class is used in ClienteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /clientes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClienteCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter identidade;

    private StringFilter cpf;

    private StringFilter email;

    private StringFilter telefone;

    private LongFilter reservaId;

    public ClienteCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getIdentidade() {
        return identidade;
    }

    public void setIdentidade(StringFilter identidade) {
        this.identidade = identidade;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public LongFilter getReservaId() {
        return reservaId;
    }

    public void setReservaId(LongFilter reservaId) {
        this.reservaId = reservaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClienteCriteria that = (ClienteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(identidade, that.identidade) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(reservaId, that.reservaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        identidade,
        cpf,
        email,
        telefone,
        reservaId
        );
    }

    @Override
    public String toString() {
        return "ClienteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (identidade != null ? "identidade=" + identidade + ", " : "") +
                (cpf != null ? "cpf=" + cpf + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (telefone != null ? "telefone=" + telefone + ", " : "") +
                (reservaId != null ? "reservaId=" + reservaId + ", " : "") +
            "}";
    }

}
