package ast.typed.expr;

import ast.typed.Type;
import ast.typed.def.method.MethodDef;
import compile.Compiler;
import compile.ScopeHelper;
import exceptions.compile_time.CompilationException;
import lexing.Loc;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public record TypedMethodCall(Loc loc, TypedExpr receiver, MethodDef method, List<TypedExpr> args, Type type) implements TypedExpr {

    @Override
    public void compile(Compiler compiler, ScopeHelper env, MethodVisitor visitor) throws CompilationException {
        receiver.compile(compiler, env, visitor);
        for (TypedExpr arg : args)
            arg.compile(compiler, env, visitor);
        method.compileCall(Opcodes.INVOKEVIRTUAL, receiver.type(), compiler, visitor);
    }
}
