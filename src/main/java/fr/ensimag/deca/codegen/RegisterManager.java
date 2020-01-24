package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class that manages the free and occupied registers.
 *
 * @author gl13
 * @date 13/01/2020
 */

public class RegisterManager {

    private int SP = 0;
    private int GB = 0;
    private int RegisterToSave = 0;
    private ArrayList<Integer> maxToSave = new ArrayList<Integer>();
    private int localVariable = 0;
    private boolean[] freeRegister;
    private int nb_registers;

    public RegisterManager(int nb_registers) {
        this.nb_registers = nb_registers;
        this.freeRegister = new boolean[nb_registers];

        for (int i = 0; i < nb_registers ; i++) {
            this.freeRegister[i] = true;
        }
    }

    // - setters
    public void setSP() { SP = 0; }

    public void setRegisterToSave() {
        maxToSave.add(RegisterToSave);
        RegisterToSave = 0;
    }

    public void setLocalVariable() { localVariable = 0; }




    // - incrementers
    public void incrSP(){
        SP++;
    }

    public void incrSP(int n){
        SP += n;
    }

    public void incrGB(){
        GB++;
    }

    public void incrGB( int n ){ GB += n; }

    public void incrRegisterToSave() {
        RegisterToSave++;
    }

    public void incrRegisterToSave(int n) { RegisterToSave += n; }

    public void incrLocalVariable() { localVariable++; }

    public void incrLocalVariable(int n) { localVariable += n; }


    // - getters
    public int getSP(){
        return SP;
    }

    public int getGB(){
        return GB;
    }

    public int getRegisterToSave() { return RegisterToSave; }

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
        incrRegisterToSave();
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

    public boolean isFreeRegister(GPRegister register){
        return freeRegister[register.getNumber()];
    }

    public void unFreeRegister(GPRegister register){
        freeRegister[register.getNumber()] = false;
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
}
