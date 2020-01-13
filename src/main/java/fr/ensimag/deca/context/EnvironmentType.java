package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;

import java.util.HashMap;
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

        // - create definitions from symbols
        // Location.BUILTIN is for predefined types
        TypeDefinition boolDefinition = new TypeDefinition(new BooleanType(boolSymbol), Location.BUILTIN);
        TypeDefinition intDefinition = new TypeDefinition(new IntType(intSymbol), Location.BUILTIN);
        TypeDefinition floatDefinition = new TypeDefinition(new FloatType(floatSymbol), Location.BUILTIN);
        TypeDefinition voidDefinition = new TypeDefinition(new VoidType(voidSymbol), Location.BUILTIN);

        // - Add types to envTypes
        this.envTypes.put(intSymbol, intDefinition);
        this.envTypes.put(boolSymbol, boolDefinition);
        this.envTypes.put(floatSymbol, floatDefinition);
        this.envTypes.put(voidSymbol, voidDefinition);

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
