package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class MethodCall extends AbstractExpr{

        private AbstractExpr instance ;
        private AbstractIdentifier methodName;
        private ListExpr params;

    public MethodCall(AbstractExpr instance, AbstractIdentifier methodName, ListExpr params){
        // instance can be null if match method(params)
        this.instance = instance;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
    	
    		
    	  //instance.verifyExpr() verifies that 'instance' exists in the local environment
    	  ClassType instanceType = instance.verifyExpr(compiler, localEnv, currentClass).asClassType("Contextual error, it is not possible to call a method on a object that is not a class instance", getLocation());    
          EnvironmentExp classInstanceEnv = instanceType.getDefinition().getMembers();
          
          //methodName.verifyExpr() verifies that 'methodName' exists in the instance's class environment
          methodName.verifyExpr(compiler, classInstanceEnv, currentClass);
         
          Signature sig = classInstanceEnv.get(methodName.getName()).asMethodDefinition("Contextual error", getLocation()).getSignature();
          
          if (sig.size() != params.getList().size()) {
  	  			throw new ContextualError("Contextual error, the method called expected " + sig.size() + " arguments but " + params.getList().size() + " were given",getLocation());
          }
          for (int i = 0; i < sig.size(); i++) {
        	  	Type paramType = params.getList().get(i).verifyExpr(compiler, localEnv, currentClass);
        	  
        	  	if (!sig.getList().get(i).sameType(paramType)){
        	  		throw new ContextualError("Contextual error, the parameters given don't respect the method signature",getLocation());
        	  	}      	  	
          }

          this.setType(classInstanceEnv.get(methodName.getName()).getType());         
    	
    	
          return this.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        methodName.decompile();
        s.print("(");
        params.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }


    protected void codeGenExpr(DecacCompiler compiler) {
        int nbParams = params.size();
        int index = 0;
        compiler.addInstruction(new TSTO(new ImmediateInteger(nbParams)));
        compiler.addInstruction(new BOV(new Label("stack_overflow")));
        compiler.addInstruction(new ADDSP(new ImmediateInteger(nbParams)));

        GPRegister register = compiler.getRegM().findFreeGPRegister();
        instance.codeGenExpr(compiler, register);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));
        compiler.addInstruction(new STORE(register, new RegisterOffset(index, Register.SP)));
        index--;

        for (AbstractExpr expr : params.getList()) {
            expr.codeGenExpr(compiler, register);
            compiler.addInstruction(new STORE(register, new RegisterOffset(index, Register.SP)));
            index--;
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), register), "On récupère le paramètre implicite");
        compiler.addInstruction(new CMP(new NullOperand(), register));
        compiler.addInstruction(new BEQ(new Label("deferencement.null")));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register), "on récupère l'adresse de la table des méthodes");
        compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex(), register)), "on saute à la méthode");
        compiler.addInstruction(new SUBSP(new ImmediateInteger(nbParams)));
    }
}
