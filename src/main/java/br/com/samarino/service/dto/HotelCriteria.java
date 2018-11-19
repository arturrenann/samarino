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
 * Criteria class for the Hotel entity. This class is used in HotelResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /hotels?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HotelCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter localizacao;

    private StringFilter cep;

    private IntegerFilter qtdAcomodacoes;

    private IntegerFilter valorDiaria;

    private LongFilter reservarId;

    public HotelCriteria() {
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

    public StringFilter getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(StringFilter localizacao) {
        this.localizacao = localizacao;
    }

    public StringFilter getCep() {
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public IntegerFilter getQtdAcomodacoes() {
        return qtdAcomodacoes;
    }

    public void setQtdAcomodacoes(IntegerFilter qtdAcomodacoes) {
        this.qtdAcomodacoes = qtdAcomodacoes;
    }

    public IntegerFilter getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(IntegerFilter valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public LongFilter getReservarId() {
        return reservarId;
    }

    public void setReservarId(LongFilter reservarId) {
        this.reservarId = reservarId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HotelCriteria that = (HotelCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(localizacao, that.localizacao) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(qtdAcomodacoes, that.qtdAcomodacoes) &&
            Objects.equals(valorDiaria, that.valorDiaria) &&
            Objects.equals(reservarId, that.reservarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        localizacao,
        cep,
        qtdAcomodacoes,
        valorDiaria,
        reservarId
        );
    }

    @Override
    public String toString() {
        return "HotelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (localizacao != null ? "localizacao=" + localizacao + ", " : "") +
                (cep != null ? "cep=" + cep + ", " : "") +
                (qtdAcomodacoes != null ? "qtdAcomodacoes=" + qtdAcomodacoes + ", " : "") +
                (valorDiaria != null ? "valorDiaria=" + valorDiaria + ", " : "") +
                (reservarId != null ? "reservarId=" + reservarId + ", " : "") +
            "}";
    }

}
