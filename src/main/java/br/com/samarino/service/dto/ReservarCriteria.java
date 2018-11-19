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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Reservar entity. This class is used in ReservarResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reservars?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservarCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter dias;

    private LocalDateFilter dataInicio;

    private LocalDateFilter dataEntrega;

    private IntegerFilter valorTotReserva;

    private LongFilter hoteisId;

    private LongFilter clienteId;

    public ReservarCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getDias() {
        return dias;
    }

    public void setDias(IntegerFilter dias) {
        this.dias = dias;
    }

    public LocalDateFilter getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateFilter dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateFilter getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateFilter dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public IntegerFilter getValorTotReserva() {
        return valorTotReserva;
    }

    public void setValorTotReserva(IntegerFilter valorTotReserva) {
        this.valorTotReserva = valorTotReserva;
    }

    public LongFilter getHoteisId() {
        return hoteisId;
    }

    public void setHoteisId(LongFilter hoteisId) {
        this.hoteisId = hoteisId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservarCriteria that = (ReservarCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dias, that.dias) &&
            Objects.equals(dataInicio, that.dataInicio) &&
            Objects.equals(dataEntrega, that.dataEntrega) &&
            Objects.equals(valorTotReserva, that.valorTotReserva) &&
            Objects.equals(hoteisId, that.hoteisId) &&
            Objects.equals(clienteId, that.clienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dias,
        dataInicio,
        dataEntrega,
        valorTotReserva,
        hoteisId,
        clienteId
        );
    }

    @Override
    public String toString() {
        return "ReservarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dias != null ? "dias=" + dias + ", " : "") +
                (dataInicio != null ? "dataInicio=" + dataInicio + ", " : "") +
                (dataEntrega != null ? "dataEntrega=" + dataEntrega + ", " : "") +
                (valorTotReserva != null ? "valorTotReserva=" + valorTotReserva + ", " : "") +
                (hoteisId != null ? "hoteisId=" + hoteisId + ", " : "") +
                (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
            "}";
    }

}
