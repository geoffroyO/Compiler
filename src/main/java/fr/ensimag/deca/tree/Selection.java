package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Selection extends AbstractLValue{
    private AbstractExpr instance;
    private AbstractIdentifier field;

    public Selection(AbstractExpr instance, AbstractIdentifier field)
    {
        this.instance = instance;
        this.field = field;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type instanceType = instance.verifyExpr(compiler, localEnv, currentClass);      
        EnvironmentExp instanceEnv = ((ClassDefinition)(compiler.getEnvTypes().get(instanceType.getName()))).getMembers();
        Type fieldType = field.verifyExpr(compiler, instanceEnv, currentClass);
             
//        if (field.getFieldDefinition().getVisibility() ==  Visibility.PROTECTED) {
//        	if (! currentClass.getType().isSubClassOf((ClassType)fieldType)) {
//        		throw new ContextualError("3.66 : Contextual error with an expression of type 'instance.field' \n"
//        				+ "'field is protected and the current class (where instance.field is called) is not a subClass of the class where 'field' is declared", this.getLocation());
//        	}
      	
//        	if (!((ClassType)(instanceType)).isSubClassOf(currentClass.getType())) {
//        		throw new ContextualError("3.66 : Contextual error with an expression of type 'instance.field' \n"
//        				+ "'field is protected and the class of 'instance' is not a subClass of the current class (where instance.field is called)", this.getLocation());
//        	}
//        }
        
        this.setType(fieldType);  
        return(this.getType());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
