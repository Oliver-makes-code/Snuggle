package ast.typed.expr;

import ast.typed.Type;
import ast.typed.def.type.BuiltinTypeDef;
import ast.typed.def.type.TypeDef;
import builtin_types.types.BoolType;
import builtin_types.types.numbers.FloatType;
import builtin_types.types.numbers.IntegerType;
import compile.Compiler;
import compile.ScopeHelper;
import exceptions.CompilationException;
import lexing.Loc;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public record TypedDeclaration(Loc loc, String name, Type type, TypedExpr rhs) implements TypedExpr {

    @Override
    public void compile(Compiler compiler, ScopeHelper env, MethodVisitor visitor) throws CompilationException {
        //First compile the rhs, pushing its result on the stack
        rhs.compile(compiler, env, visitor);

        //Dup the result value
        if (compiler.getTypeDef(type) instanceof BuiltinTypeDef b) {
            if (b.builtin() instanceof IntegerType i && i.bits == 64)
                visitor.visitInsn(Opcodes.DUP2);
            else if (b.builtin() instanceof FloatType f && f.bits == 64)
                visitor.visitInsn(Opcodes.DUP2);
            else
                visitor.visitInsn(Opcodes.DUP);
        } else {
            visitor.visitInsn(Opcodes.DUP);
        }

        //Store
        int index = env.declare(loc, compiler, name, type);
        TypeDef def = compiler.getTypeDef(type);
        TypedVariable.visitVariable(index, def, true, visitor);
    }
}
