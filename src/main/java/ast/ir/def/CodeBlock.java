package ast.ir.def;

import ast.ir.helper.ScopeHelper;
import ast.ir.instruction.Instruction;
import exceptions.compile_time.CompilationException;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import runtime.SnuggleInstance;
import runtime.SnuggleRuntime;

import java.util.ArrayList;

/**
 * A sequence of instructions to be executed in order.
 */
public class CodeBlock {

    //The instructions in the array
    private final ArrayList<Instruction> instructions = new ArrayList<>();
    private int cost = 0;

    //The scope for this code block
    public final ScopeHelper env;

    public CodeBlock() {
        env = new ScopeHelper();
    }

    public CodeBlock(CodeBlock other) {
        env = other.env;
        cost = 0; //Cost is tracked independently
    }

    public CodeBlock emit(Instruction instruction) {
        //TODO: Peephole-optimize while we add instructions, so we don't have to copy lots of things backwards in memory
        instructions.add(instruction);
        cost += instruction.cost();
        return this;
    }

    //Get cost
    public int cost() { return cost; }

    //Returns the "cost" of the script as calculated by the IR
    public void writeJvmBytecode(MethodVisitor jvmBytecode) throws CompilationException {
        //TODO: Make this not just be some random static field
        jvmBytecode.visitLdcInsn(cost);
        jvmBytecode.visitFieldInsn(Opcodes.GETSTATIC, Type.getInternalName(SnuggleInstance.class), "INSTRUCTIONS", "I");
        jvmBytecode.visitInsn(Opcodes.IADD);
        jvmBytecode.visitFieldInsn(Opcodes.PUTSTATIC, Type.getInternalName(SnuggleInstance.class), "INSTRUCTIONS", "I");

        for (Instruction i : instructions)
            i.accept(jvmBytecode);
    }


}
