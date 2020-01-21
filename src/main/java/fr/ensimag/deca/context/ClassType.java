package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        return(this.toString().equals(otherType.toString()));
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        Type typeToCheck = potentialSuperClass.getDefinition().getType();
        return( this.checkIsChild(typeToCheck) );
    }

    /**
     * Return true if otherType is a superclass of this class.
     */
    public boolean checkIsChild(Type otherType) {

        ClassDefinition currentDefinition = this.getDefinition();
        while (currentDefinition != null) {
            // - check if current class equals the class passed in argument
            if (currentDefinition.getType().getName().getName().equals(otherType.getName().getName())) {
                return true;
            }
            // - go up to super class
            currentDefinition = currentDefinition.getSuperClass();
        }
        return false;
    }


}
