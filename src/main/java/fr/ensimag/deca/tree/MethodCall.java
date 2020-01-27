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
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

public class MethodCall extends AbstractLValue {

	private AbstractExpr instance;
	private AbstractIdentifier methodName;
	private ListExpr params;

	public MethodCall(AbstractExpr instance, AbstractIdentifier methodName, ListExpr params) {
		// instance can be null if match method(params)
		this.instance = instance;
		this.methodName = methodName;
		this.params = params;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {

		// instance.verifyExpr() verifies that 'instance' exists in the local
		// environment
		ClassType instanceType = instance.verifyExpr(compiler, localEnv, currentClass).asClassType(
				"Contextual error, it is not possible to call a method on a object that is not a class instance",
				getLocation());
		EnvironmentExp classInstanceEnv = instanceType.getDefinition().getMembers();

		// methodName.verifyExpr() verifies that 'methodName' exists in the instance's
		// class environment
		methodName.verifyExpr(compiler, classInstanceEnv, currentClass);

		Signature sig = classInstanceEnv.get(methodName.getName()).asMethodDefinition("Contextual error", getLocation())
				.getSignature();

		if (sig.size() != params.getList().size()) {
			throw new ContextualError("Contextual error, the method called expected " + sig.size() + " arguments but "
					+ params.getList().size() + " were given", getLocation());
		}
		for (int i = 0; i < sig.size(); i++) {
			Type paramType = params.getList().get(i).verifyExpr(compiler, localEnv, currentClass);
			Type sigType = sig.getList().get(i);
			
			// If we are dealing with classes, the type given in a method call don't need to
			// be the same as the signature, they can also be subclasses.
			if (sigType.isClass()) {
				if (!paramType.asClassType("CE", getLocation()).isSubClassOf(sigType.asClassType("CE", getLocation()))){
					throw new ContextualError("Contextual error, the parameters given don't respect the method signature (classType error)",
							getLocation());
				}
				else {
					continue;
				}
			}

			if (!sig.getList().get(i).sameType(paramType)) {
				throw new ContextualError("Contextual error, the parameters given don't respect the method signature",
						getLocation());
			}
		}
		this.setType(classInstanceEnv.get(methodName.getName()).getType());
		return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		instance.decompile(s);
		s.print(".");
		methodName.decompile(s);
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

	private void codeGenMethodCall(DecacCompiler compiler, GPRegister register) {
		// - test stack overflow, don't forget the implicit parameter
		compiler.TSTO(params.size() + 1);
		compiler.addInstruction(new ADDSP(params.size() + 1));

		// - load the implicit parameter in the stack
		instance.codeGenExpr(compiler, register);
		compiler.addInstruction(new STORE(register, new RegisterOffset(0, Register.SP)));

		// - load the other parameters in the stack
		int indexSp = -1;

		for (AbstractExpr expr : params.getList()) {

			// - get the value into register
			expr.codeGenExpr(compiler, register);
			compiler.addInstruction(new STORE(register, new RegisterOffset(indexSp, Register.SP)));
			indexSp--;
		}

		// - test reference for implicit parameter
		compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), register));
		compiler.referenceErr(register);

		// - get the address of the table of methods
		compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));

		// - set a free register table for the subprogram
		boolean[] oldFreeRegisters = compiler.getRegM().resetFreeRegister();

		// - get the final address of the method called and jump to it
		compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex(), register)));

		// - reset the old register table
		compiler.getRegM().setFreeRegister(oldFreeRegisters);

		// - unstack the parameters
		compiler.addInstruction(new SUBSP(params.size() + 1));
	}

	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		// - generate the code to call the function, the result is in R0
		codeGenMethodCall(compiler, register);

		// - STORE the result in register
		compiler.addInstruction(new LOAD(Register.R0, register));
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		// - temporary register where we put nothing
		GPRegister register = compiler.getRegM().findFreeGPRegister();

		// - generate the code to call the function, the result is in R0
		codeGenMethodCall(compiler, register);

		// - free the register
		compiler.getRegM().freeRegister(register);
	}

	@Override
	protected void codeGenLValueAddr(DecacCompiler compiler, GPRegister register) {
		// - generate the code to call the function, the result is in R0
		codeGenMethodCall(compiler, register);

		// - load the address in register
		compiler.addInstruction(new LEA(new RegisterOffset(0, Register.R0), register));
	}

	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		// - generate the code to call the function and put the value in R1
		codeGenMethodCall(compiler, Register.R1);
		compiler.addInstruction(new LOAD(Register.R0, Register.R1));

		super.codeGenPrint(compiler, printHex);
	}
}
