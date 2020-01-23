package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Class that manages the free and occupied registers.
 *
 * @author gl13
 * @date 13/01/2020
 */

public class RegisterManager {

    private int SP;
    private int GB;
    private int LB;
    private int RegisterToSave = 0;
    private boolean[] freeRegister;
    private int nb_registers;

    public RegisterManager(int nb_registers) {
        this.SP = 0;
        this.GB = 0;
        this.LB = 0;
        this.nb_registers = nb_registers;
        this.freeRegister = new boolean[nb_registers];

        for (int i = 0; i < nb_registers ; i++) {
            this.freeRegister[i] = true;
        }
    }

    public void incrRegisterToSave() { RegisterToSave++; }

    public void incrRegisterToSave(int n) { RegisterToSave += n; }

    public void setRegisterToSave() { RegisterToSave = 0; }

    public int getRegisterToSave() { return RegisterToSave; }

    public int getNb_registers(){
        return this.nb_registers - 1;
    }

    public void incrSP(){
        SP++;
    }

    public void incrSP(int n){
        SP += n;
    }

    public void setSP() { SP = 0; }

    public void incrGB(){
        GB++;
    }

    public void incrGB( int n ){
        GB += n;
    }

    public void incrLB(){
        LB++;
    }

    public int getSP(){
        return SP;
    }

    public int getGB(){
        return GB;
    }

    public int getLB(){
        return LB;
    }

    public GPRegister findFreeGPRegister(){
        // TODO faire un bon rattrapage d'erreur
        int j = -1;

        for (int i = nb_registers - 1; i >= 2; i--) {
            if (freeRegister[i]) {
                j = i;
            }
        }
        this.freeRegister[j] = false;
        return Register.getR(j);
    }

    public boolean hasFreeGPRegister(){
        for (int i = nb_registers - 1; i >= 2; i--){

            if (freeRegister[i]){
                return true;
            }
        }
        return false;
    }

    public void freeRegister(GPRegister register){
        int i = register.getNumber();
        freeRegister[i] = true;
    }

    public boolean isFreeRegister(GPRegister register){
        return freeRegister[register.getNumber()];
    }

    public void unFreeRegister(GPRegister register){
        freeRegister[register.getNumber()] = false;
    }

    public boolean[] setFreeRegister() {
        boolean[] oldFreeRegister = Arrays.copyOf(freeRegister, freeRegister.length);
        for (int i = 0; i < nb_registers ; i++) {
            this.freeRegister[i] = true;
        }
        return oldFreeRegister;
    }
}
