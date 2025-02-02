package ast.type_resolved.expr;

import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.type_resolved.def.field.TypeResolvedFieldDef;
import ast.typed.def.type.TypeDef;
import ast.typed.expr.TypedExpr;
import ast.typed.expr.TypedVariable;
import exceptions.compile_time.CompilationException;
import exceptions.compile_time.TypeCheckingException;
import exceptions.compile_time.UndeclaredVariableException;
import lexing.Loc;

import java.util.List;


public record TypeResolvedVariable(Loc loc, String name) implements TypeResolvedExpr {
    @Override
    public void verifyGenericArgCounts(GenericVerifier verifier) throws CompilationException {
        //do nothing
    }

    @Override
    public TypedExpr infer(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics) throws CompilationException {
        TypeDef t = checker.lookup(name);
        if (t == null) {
            if (!name.equals("this") && checker.lookup("this") != null) {
                return new TypeResolvedFieldAccess(loc, new TypeResolvedVariable(loc, "this"), name, true).infer(currentType, checker, typeGenerics);
            }
            throw new UndeclaredVariableException("Variable \"" + name + "\" was not declared in this scope", loc);
        } else {
            return new TypedVariable(loc, name, t);
        }
    }

    @Override
    public TypedExpr check(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics, TypeDef expected) throws CompilationException {
        TypedExpr e = infer(currentType, checker, typeGenerics);
        if (!e.type().isSubtype(expected))
            throw new TypeCheckingException("Expected " + expected.name() + ", got " + e.type().name(), loc);
        return e;
    }
}
