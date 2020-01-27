package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Assign extends AbstractBinaryExpr {

	@Override
	public AbstractLValue getLeftOperand() {
		// The cast succeeds by construction, as the leftOperand has been set
		// as an AbstractLValue by the constructor.
		return (AbstractLValue) super.getLeftOperand();
	}

	public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {

		Type LeftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
		// - check for integer to float conversion or for different type
		this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, LeftOpType));
		this.setType(LeftOpType);
		return LeftOpType;
	}

	@Override
	protected String getOperatorName() {
		return "=";
	}


	/**
	 * Generates assembly code to evaluate the expression.
	 *
	 * @param compiler
	 *
	 * @param register
	 * 		The result of the expression is stored in register.
	 *
	 */
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		if (compiler.getRegM().hasFreeGPRegister()) {
			// - register that store the address of the left operand to assign
			GPRegister addrReg = compiler.getRegM().findFreeGPRegister();
			getLeftOperand().codeGenLValueAddr(compiler, addrReg);

			// - the value of the right operand is in register
			getRightOperand().codeGenExpr(compiler, register);

			compiler.addInstruction(new STORE(register, new RegisterOffset(0, addrReg)));

			// - free the register
			compiler.getRegM().freeRegister(addrReg);
		} else {
			// TODO
		}
	}


	/**
	 * Generates assembly code to execute the instructions.
	 *
	 * @param compiler
	 *
	 */
	protected void codeGenInst(DecacCompiler compiler) {
		// - register that store the address of the left operand to assign
		GPRegister addrReg = compiler.getRegM().findFreeGPRegister();
		getLeftOperand().codeGenLValueAddr(compiler, addrReg);

		GPRegister register = compiler.getRegM().findFreeGPRegister();

		// - the value of the right operand is in register
		getRightOperand().codeGenExpr(compiler, register);

		compiler.addInstruction(new STORE(register, new RegisterOffset(0, addrReg)));

		// - free the registers
		compiler.getRegM().freeRegister(register);
		compiler.getRegM().freeRegister(addrReg);
	}
}
