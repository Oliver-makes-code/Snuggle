package ast.typed.expr;

import ast.ir.def.CodeBlock;
import ast.ir.instruction.MethodCall;
import ast.ir.instruction.misc.LineNumber;
import ast.ir.instruction.misc.New;
import ast.typed.def.method.MethodDef;
import ast.typed.def.type.BuiltinTypeDef;
import ast.typed.def.type.TypeDef;
import exceptions.compile_time.CompilationException;
import lexing.Loc;

import java.util.List;

public record TypedConstructor(Loc loc, TypeDef type, MethodDef method, List<TypedExpr> args) implements TypedExpr {

    @Override
    public void compile(CodeBlock block) {

        if (!type.hasSpecialConstructor()) {
            //If this type doesn't have special constructor procedures:
            block.emit(new New(type.get())); //Emit a NEW, and dup the value. This is essentially compiling "the receiver".
        }

        for (TypedExpr arg : args)
            arg.compile(block);
        block.emit(new LineNumber(loc.startLine()));
        block.emit(new MethodCall(false, method));
    }
}
