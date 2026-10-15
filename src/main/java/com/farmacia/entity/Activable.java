package com.farmacia.entity;

/** Entidades con baja lógica: DELETE no borra la fila, solo marca activo = false. */
public interface Activable {
    void setActivo(Boolean activo);
}
