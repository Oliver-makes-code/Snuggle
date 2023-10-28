package ast.ir.instruction.flow;

import ast.ir.instruction.Instruction;
import exceptions.compile_time.CompilationException;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public record JumpIfFalse(Label label) implements Instruction {

    @Override
    public void accept(MethodVisitor jvm) {
        jvm.visitJumpInsn(Opcodes.IFEQ, label);
    }

    @Override
    public int cost() {
        return 1;
    }
}
