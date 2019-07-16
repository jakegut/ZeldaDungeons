package me.jakerg.gdxtest.creature;

public class Creature {

    private int hp;
    private int maxHP;

    public Creature(int max) {
        this.hp = max;
        this.maxHP = max;
    }

    public int hp() {
        return hp;
    }

    public void decHP() {
        hp--;
    }

    public int maxHP() {
        return maxHP;
    }
}
