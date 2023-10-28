package ast.typed.expr;

import ast.ir.def.CodeBlock;
import ast.ir.instruction.vars.LoadLocal;
import ast.ir.instruction.vars.StoreLocal;
import ast.typed.def.type.TypeDef;
import exceptions.compile_time.CompilationException;
import lexing.Loc;

public record TypedAssignment(Loc loc, TypedExpr lhs, TypedExpr rhs, TypeDef type) implements TypedExpr {

    //Largely works similarly to TypedDeclaration.compile()
    @Override
    public void compile(CodeBlock code) {

        if (lhs instanceof TypedVariable variable) {
            rhs.compile(code); //Push rhs on the stack
            int index = code.env.lookup(loc, variable.name()); //Lookup index
            code.emit(new StoreLocal(index, type.get())); //Emit assignment
            code.emit(new LoadLocal(index, type.get())); //Load the local
        } else if (lhs instanceof TypedFieldAccess fieldAccess) {
            throw new IllegalStateException("Field assignment not yet re-implemented");
        } else {
            throw new IllegalStateException("Attempting assignment to something other than var or field? Bug in compiler, please report!");
        }
    }
}
