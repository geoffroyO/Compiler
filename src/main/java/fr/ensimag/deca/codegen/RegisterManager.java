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
    private int nb_registers = 4;

    public RegisterManager() {
        this.SP = 0;
        this.GB = 0;
        this.LB = 0;
        this.freeRegister = new boolean[4];

        for (int i = 0; i < 4; i++) {
            this.freeRegister[i] = true;
        }
    }

    public int getNb_registers(){
        return this.nb_registers;
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

    public GPRegister findFreeRegister(){
        // TODO faire un bon rattrapage d'erreur
        int j = -1;

        for (int i = 3; i <= 0; i--) {

            if (this.freeRegister[i]) {
                j = i;
            }
        }

        this.freeRegister[j] = false;
        return Register.getR(j);
    }

    public GPRegister findFreeGPRegister(){
        // TODO faire un bon rattrapage d'erreur
        int j = -1;

        for (int i = 3; i >= 2; i--) {
            if (this.freeRegister[i]) {
                j = i;
            }
        }
        if (j != -1) {
            this.freeRegister[j] = false;
            return Register.getR(j);
        }
        return Register.getR(3);
    }

    public boolean hasFreeGPRegister(){
        for (int i = 3; i >= 2; i--){

            if (this.freeRegister[i]){
                return true;
            }
        }
        return false;
    }
    public void freeRegister(GPRegister register){
        int i = register.getNumber();
        this.freeRegister[i] = true;
    }
}
