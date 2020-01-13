package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

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
    private boolean[] freeRegister;

    public RegisterManager() {
        this.SP = 0;
        this.GB = 0;
        this.LB = 0;
        this.freeRegister = new boolean[15];

        for (int i = 0; i < 15; i++) {
            this.freeRegister[i] = true;
        }
    }

    public void incrSP(){
        this.SP++;
    }

    public void incrGB(){
        this.GB++;
    }

    public void incrLB(){
        this.LB++;
    }

    public int getSP(){
        return this.SP;
    }

    public int getGB(){
        return this.GB;
    }

    public int getLB(){
        return this.LB;
    }
    public boolean isFreeRegister() {

        for (int i = 0; i < 15; i++) {
            if (this.freeRegister[i]) {
                return true;
            }
        }

        return false;
    }

    public GPRegister findFreeRegister(){
        // TODO faire un bon rattrapage d'erreur
        int j = -1;

        for (int i = 0; i < 15; i++) {

            if (this.freeRegister[i]) {
                j = i;
            }
        }

        return Register.getR(j);
    }

    public GPRegister findFreeGPRegister(){
        // TODO faire un bon rattrapage d'erreur
        int j = -1;

        for (int i = 2; i < 15; i++) {
            if (this.freeRegister[i]) {
                j = i;
            }
        }

        return Register.getR(j);
    }

    public void freeRegister(GPRegister register){
        int i = register.getNumber();
        this.freeRegister[i] = true;
    }
}
