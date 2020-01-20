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

    public int getNb_registers(){
        return this.nb_registers - 1;
    }

    public void incrSP(){
        this.SP++;
    }

    public void incrSP(int n){
        this.SP = this.SP + n;
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

    public GPRegister findFreeGPRegister(){
        // TODO faire un bon rattrapage d'erreur
        int j = -1;

        for (int i = nb_registers - 1; i >= 2; i--) {
            if (this.freeRegister[i]) {
                j = i;
            }
        }
        this.freeRegister[j] = false;
        return Register.getR(j);
    }

    public boolean hasFreeGPRegister(){
        for (int i = nb_registers - 1; i >= 2; i--){

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
