package com.example.taksmaster;

import java.io.Serializable;

public class SubTarea implements Serializable {
    private String nombre;
    private boolean completada;

    public SubTarea(String nombre) {
        this.nombre = nombre;
        this.completada = false;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
