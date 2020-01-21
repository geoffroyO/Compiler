package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class that manages the methods of a class.
 *
 * @author gl13
 * @date 21/01/2020
 */

public class MethodsTable {

    // - Structure to store the environment (Label -> MethodDefinition)
    private Map<Integer, MethodDefinition> methodsTable;

    /**
     * [CREATE] the methods table
     */
    public MethodsTable() {
        this.methodsTable = new HashMap<Integer, MethodDefinition>();
    }

    /**
     * [GET] the methods table
     */
    public Map<Integer, MethodDefinition> getMT() {
        return methodsTable;
    }

    /**
     * [ADD] a MethodDefinition to MT
     */
    public void putInMT(MethodDefinition methodDefinition){
        methodsTable.put(methodDefinition.getIndex(), methodDefinition);
    }

    /**
     * [GET] a MethodDefinition from MT using an index
     */
    public MethodDefinition getFromMT(int index){
        return methodsTable.get(index);
    }

    /**
     * [UPDATE] the methods table with super classes methods
     */
    public void superUpdateMT(ClassDefinition superClass) {
        if (superClass != null) {
            Map<Integer, MethodDefinition> superMethodsTable = superClass.getMT().getMT();
            for (Map.Entry<Integer, MethodDefinition> integerMethodDefinitionEntry : superMethodsTable.entrySet()) {
                MethodDefinition method = (MethodDefinition) ((Map.Entry) integerMethodDefinitionEntry).getValue();
                if (methodsTable.get(method.getIndex()) == null) {
                    this.putInMT(method);
                }
            }
        }
        if (superClass.getSuperClass() != null){
            ClassDefinition superSuperClass = superClass.getSuperClass();
            this.superUpdateMT(superSuperClass);
        }

    }


    /**
     * [PRINT] the methods table
     */
    @Override
    public String toString() {
        String s = "======== Methods Table ======== [size = " + methodsTable.size() + "] \n";
        for (Map.Entry<Integer, MethodDefinition> integerMethodDefinitionEntry : methodsTable.entrySet()) {
            MethodDefinition method = (MethodDefinition) ((Map.Entry) integerMethodDefinitionEntry).getValue();
            s += "index = " + method.getIndex() + " | Label = " + method.getLabel().toString();
            s += "\n";
        }
        s += "===============================";
        return s;
    }
}
