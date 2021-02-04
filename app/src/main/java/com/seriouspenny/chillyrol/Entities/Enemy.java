package com.seriouspenny.chillyrol.Entities;

public class Enemy extends Creature {
    private int danoRecibido;

    public Enemy() {
        super();
        this.danoRecibido = 0;
    }

    public Enemy(String nombre, int danoRecibido, int iniciativa, Status estado, int pifias) {
        super(nombre, iniciativa, estado, pifias);
        this.danoRecibido = danoRecibido;
    }

    public int getDanoRecibido() {
        return danoRecibido;
    }

    public void setDanoRecibido(int danoRecibido) {
        this.danoRecibido = danoRecibido;
    }

    public void cambiarDanoRecibido(int danoRecibido)
    {
        this.danoRecibido += danoRecibido;
    }
}
