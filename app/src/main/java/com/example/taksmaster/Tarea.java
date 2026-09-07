package com.example.taksmaster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tarea implements Serializable {
    private String nombre;
    private String categoria;
    private String prioridad;
    private boolean completada;
    private float importancia;
    private int progreso;
    private List<SubTarea> subTareas;

    public Tarea(String nombre, String categoria, String prioridad, boolean completada, float importancia, int progreso) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.completada = completada;
        this.importancia = importancia;
        this.progreso = progreso;
        this.subTareas = new ArrayList<>();
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
        if (subTareas != null && !subTareas.isEmpty()) {
            int completadas = 0;
            for (SubTarea st : subTareas) {
                if (st.isCompletada()) completadas++;
            }
            return (completadas * 100) / subTareas.size();
        }
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public List<SubTarea> getSubTareas() {
        return subTareas;
    }

    public void setSubTareas(List<SubTarea> subTareas) {
        this.subTareas = subTareas;
    }
}
