package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

/**
 *
 * @author Ensimag
 * @date 01/01/2020
 */
public class FLOAT extends BinaryInstructionDValToReg {

    public FLOAT(DVal op1, GPRegister op2) {
        super(op1, op2);
    }
    
    public FLOAT(float f, GPRegister r) {
        this(new ImmediateFloat(f), r);
    }
}
