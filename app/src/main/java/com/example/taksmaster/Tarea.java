package com.example.taksmaster;

import java.io.Serializable;

public class Tarea implements Serializable {
    private String nombre;
    private String categoria;
    private String prioridad;
    private boolean completada;
    private float importancia;
    private int progreso;

    public Tarea(String nombre, String categoria, String prioridad, boolean completada, float importancia, int progreso) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.completada = completada;
        this.importancia = importancia;
        this.progreso = progreso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public float getImportancia() {
        return importancia;
    }

    public void setImportancia(float importancia) {
        this.importancia = importancia;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }
}
