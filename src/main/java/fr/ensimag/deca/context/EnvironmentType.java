package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EnvironmentType {

    private Map<Symbol, TypeDefinition> envTypes;

    /**
     * Initialize the env_types with predefined types
     */
    public EnvironmentType(SymbolTable symbols) {

        this.envTypes =  new HashMap<Symbol, TypeDefinition>();

        // - create symbols for predefined types
        Symbol boolSymbol = symbols.create("boolean");
        Symbol intSymbol = symbols.create("int");
        Symbol floatSymbol = symbols.create("float");
        Symbol voidSymbol = symbols.create("void");
        Symbol objectSymbol = symbols.create("Object");
        Symbol equalsSymbol = symbols.create("equals");

        // - create definitions from symbols
        // Location.BUILTIN is for ** PREDEFINED ** types
        TypeDefinition boolDefinition = new TypeDefinition(new BooleanType(boolSymbol), Location.BUILTIN);
        TypeDefinition intDefinition = new TypeDefinition(new IntType(intSymbol), Location.BUILTIN);
        TypeDefinition floatDefinition = new TypeDefinition(new FloatType(floatSymbol), Location.BUILTIN);
        TypeDefinition voidDefinition = new TypeDefinition(new VoidType(voidSymbol), Location.BUILTIN);

        // - ** Object **
        // - create a classType for object and then create a TypeDefinition (ClassDefinition) for it
        ClassType objectType = new ClassType(objectSymbol, Location.BUILTIN, null);
        ClassDefinition objectDefinition = objectType.getDefinition();

        // - create a signature for method equals(Object o) method of Object
        Signature equalsSignature = new Signature();
        // - add an argument (objectType) for this method since equals(Object o)
        equalsSignature.add(objectType);

        // - create a method definition for 'boolean Object.equals(Object o)' using the type and signature
        MethodDefinition equalsDefinition = new MethodDefinition(new BooleanType(boolSymbol), Location.BUILTIN, equalsSignature, 1);
        equalsDefinition.setLabel(new Label("code.Object.equals"));

        // - add method "equals" to methods table of Object
        objectDefinition.getMT().putInMT(equalsDefinition);

        try{
            // - add equals method to env_exp of Object
            objectDefinition.getMembers().declare(equalsSymbol, equalsDefinition);
            // - increase the number of methods for Object class
            objectDefinition.incNumberOfMethods();

        } catch (EnvironmentExp.DoubleDefException doubleDef){}

        try {
            // - Add types to envTypes
            declare(intSymbol, intDefinition);
            declare(boolSymbol, boolDefinition);
            declare(floatSymbol, floatDefinition);
            declare(voidSymbol, voidDefinition);
            declare(objectSymbol, objectDefinition);
        } catch (EnvironmentType.DoubleDefException doubleDef){}


//        Iterator it = this.envTypes.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry i = (Map.Entry)it.next();
//            System.out.println(i.getValue().getClass().getName());
//        }

    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        return this.envTypes.get(key);
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     *
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     *
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {

        // - add the type definition associated with the symbol to the environment if it's not in the environment
        if (this.envTypes.containsKey(name)) {
            throw new DoubleDefException();
        }else{
            this.envTypes.put(name, def);
        }

    }

}
