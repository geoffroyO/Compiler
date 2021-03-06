package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ClassDefinition extends TypeDefinition {


    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    private DAddr addrClass = null;

    public void setAddrClass(DAddr addr) {
        addrClass = addr;
    }

    public DAddr getAddrClass() {
        return addrClass;
    }
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass;

    // - Methods Table
    private final MethodsTable MT;

    public MethodsTable getMT() {
        return MT;
    }

    public EnvironmentExp getMembers() {
        return members;
    }

    public int getAllLevelsMembersNumber(){
        if (superClass != null){
            return superClass.numberOfMethods + superClass.getAllLevelsMembersNumber();
        } else {
            return 0;
        }
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
        this.MT = new MethodsTable();
    }
    
}
