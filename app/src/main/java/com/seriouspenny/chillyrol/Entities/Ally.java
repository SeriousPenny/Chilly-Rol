package com.seriouspenny.chillyrol.Entities;

public class Ally extends Creature {
    private int vidaActual, vidaMaxima;

    public Ally() {
        super();
        this.vidaActual = 1;
        this.vidaMaxima = 1;
    }

    public Ally(String nombre, int vidaActual, int vidaMaxima, int iniciativa, Status estado, int pifias) {
        super(nombre, iniciativa, estado, pifias);
        this.vidaActual = vidaActual;
        this.vidaMaxima = vidaMaxima;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public void cambiarVida(int vidaASumarORestar)
    {
        this.vidaActual += vidaASumarORestar;
    }
}
