package com.seriouspenny.chillyrol.Entities;

import com.google.gson.annotations.SerializedName;

public class Creature {
    private static int idGenerator = 0;
    private String nombre;
    private int iniciativa, id;
    private Status estado;
    private int pifias;
    private boolean colocadaEnCanvas; //Boolean used to know if it's been placed in the canvas
    private float canvasX, canvasY;

    @SerializedName("type")
    private String typeName;

    public Creature()
    {
        this.id = idGenerator++;
        this.nombre = "";
        this.iniciativa = 1;
        this.estado = Status.NORMAL;
        this.pifias = 0;
        this.typeName = getClass().getName();
        this.colocadaEnCanvas = false;
        this.canvasX = 0;
        this.canvasY = 0;
    }

    public Creature(String nombre, int iniciativa, Status estado, int pifias)
    {
        this.id = idGenerator++;
        this.nombre = nombre;
        this.iniciativa = iniciativa;
        this.estado = estado;
        this.pifias = pifias;
        this.typeName = getClass().getName();
        this.colocadaEnCanvas = false;
        this.canvasX = 0;
        this.canvasY = 0;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIniciativa() {
        return iniciativa;
    }

    public void setIniciativa(int iniciativa) {
        this.iniciativa = iniciativa;
    }

    public Status getEstado() {
        return estado;
    }

    public void setEstado(Status estado) {
        this.estado = estado;
    }

    public int getPifias() {
        return pifias;
    }

    public void setPifias(int pifias) {
        this.pifias = pifias;
    }

    public void cambiarPifia(int pifiaASumarORestar)
    {
        this.pifias += pifiaASumarORestar;
    }

    public boolean isColocadaEnCanvas() {
        return colocadaEnCanvas;
    }

    public void setColocadaEnCanvas(boolean colocadaEnCanvas) {
        this.colocadaEnCanvas = colocadaEnCanvas;
    }

    public float getCanvasX() {
        return canvasX;
    }

    public void setCanvasX(float canvasX) {
        this.canvasX = canvasX;
    }

    public float getCanvasY() {
        return canvasY;
    }

    public void setCanvasY(float canvasY) {
        this.canvasY = canvasY;
    }
}
