package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class that manages the free and occupied registers.
 *
 * @author gl13
 * @date 13/01/2020
 */

public class RegisterManager {

    private int SP = 0;
    private int LB = 1;
    private int RegisterToSave = 0;
    private ArrayList<Integer> maxToSave = new ArrayList<Integer>();
    private int localVariable = 0;
    private boolean[] freeRegister;
    private int nb_registers;
    private Register base = Register.GB;

    public RegisterManager(int nb_registers) {
        this.nb_registers = nb_registers;
        this.freeRegister = new boolean[nb_registers];

        for (int i = 0; i < nb_registers ; i++) {
            this.freeRegister[i] = true;
        }
    }

    // - setters
    public void setSP() { SP = 0; }

    public void setLB() { LB = 1; }
    public void setLB(int n) { LB = n; }

    public void setRegisterToSave() {
        maxToSave.add(RegisterToSave);
        RegisterToSave = 0;
    }

    public void setLocalVariable() { localVariable = 0; }


    // - increments

    public void incSP(int n){
        SP += n;
    }

    public void incLB(){
        LB++;
    }

    public void incLB(int n ){ LB += n; }

    public void incRegisterToSave() {
        RegisterToSave++;
    }

    public void incLocalVariable(int n) { localVariable += n; }


    // - getters
    public Register getBase() { return base;}

    public int getSP(){
        return SP;
    }

    public int getLB(){
        return LB;
    }

    public int getLocalVariable() { return localVariable; }

    public int getNb_registers(){ return nb_registers - 1; }

    public int getMaxToSave() {
        int max = Collections.max(maxToSave);
        maxToSave = new ArrayList<Integer>();
        return max;
    }

    // - find a GPRegister that is free
    public GPRegister findFreeGPRegister(){
        int j = -1;

        for (int i = nb_registers - 1; i >= 2; i--) {
            if (freeRegister[i]) {
                j = i;
            }
        }
        this.freeRegister[j] = false;
        incRegisterToSave();
        return Register.getR(j);
    }

    // - find a GPRegister that is free
    public boolean hasFreeGPRegister(){
        for (int i = nb_registers - 1; i >= 2; i--){

            if (freeRegister[i]){
                return true;
            }
        }
        return false;
    }

    public void freeRegister(GPRegister register){
        setRegisterToSave();
        int i = register.getNumber();
        freeRegister[i] = true;
    }

    public boolean[] resetFreeRegister() {
        boolean[] oldFreeRegister = Arrays.copyOf(freeRegister, freeRegister.length);
        for (int i = 0; i < nb_registers ; i++) {
            this.freeRegister[i] = true;
        }
        return oldFreeRegister;
    }

    public void  setFreeRegister(boolean[] oldFreeRegister){
        freeRegister = Arrays.copyOf(oldFreeRegister, oldFreeRegister.length);
    }

    public void changeBase() {
        if (base == Register.GB) {
            base = Register.LB;
        } else {
            base = Register.GB;
        }
    }
}
