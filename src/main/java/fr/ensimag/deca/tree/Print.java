package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class Print extends AbstractPrint {
    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printx)
     */
    public Print(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    String getSuffix() {

        return "";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        super.codeGenInst(compiler);
    }
}
