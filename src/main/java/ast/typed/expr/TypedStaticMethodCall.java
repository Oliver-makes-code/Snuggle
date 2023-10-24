package ast.typed.expr;

import ast.typed.Type;
import ast.typed.def.method.MethodDef;
import compile.Compiler;
import compile.ScopeHelper;
import exceptions.compile_time.CompilationException;
import lexing.Loc;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public record TypedStaticMethodCall(Loc loc, Type receiverType, MethodDef method, List<TypedExpr> args, Type type) implements TypedExpr {

    @Override
    public void compile(Compiler compiler, ScopeHelper env, MethodVisitor visitor) throws CompilationException {
        for (TypedExpr arg : args)
            arg.compile(compiler, env, visitor);

        //Visit the line number:
        Label label = new Label();
        visitor.visitLabel(label);
        visitor.visitLineNumber(loc.startLine(), label);

        method.compileCall(Opcodes.INVOKESTATIC, receiverType, compiler, visitor);
    }
}
