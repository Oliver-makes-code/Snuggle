package ast.ir.instruction.flow;

import ast.ir.instruction.Instruction;
import ast.typed.def.method.MethodDef;
import ast.typed.def.type.TypeDef;
import builtin_types.types.BoolType;
import builtin_types.types.UnitType;
import builtin_types.types.numbers.FloatType;
import builtin_types.types.numbers.IntegerType;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public record Return(MethodDef methodDef, TypeDef returnType) implements Instruction {

    @Override
    public void accept(MethodVisitor jvm) {

        if (returnType.builtin() == UnitType.INSTANCE) {
            if (methodDef.isConstructor()) {
                //Constructors can't return any value in the jvm
                jvm.visitInsn(Opcodes.POP);
                jvm.visitInsn(Opcodes.RETURN);
            } else {
                jvm.visitInsn(Opcodes.ARETURN);
            }
        } else if (returnType.builtin() instanceof IntegerType i) {
            if (i.bits <= 32)
                jvm.visitInsn(Opcodes.IRETURN);
            else
                jvm.visitInsn(Opcodes.LRETURN);
        } else if (returnType.builtin() instanceof FloatType f) {
            if (f.bits == 32)
                jvm.visitInsn(Opcodes.FRETURN);
            else
                jvm.visitInsn(Opcodes.DRETURN);
        } else if (returnType.builtin() == BoolType.INSTANCE)
            jvm.visitInsn(Opcodes.IRETURN);
        else
            jvm.visitInsn(Opcodes.ARETURN);

    }

    @Override
    public int cost() {
        return 1;
    }
}
