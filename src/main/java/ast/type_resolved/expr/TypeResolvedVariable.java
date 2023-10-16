package ast.type_resolved.expr;

import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.typed.Type;
import ast.typed.expr.TypedExpr;
import ast.typed.expr.TypedVariable;
import exceptions.CompilationException;
import exceptions.TypeCheckingException;
import lexing.Loc;

import java.util.List;

public record TypeResolvedVariable(Loc loc, String name) implements TypeResolvedExpr {
    @Override
    public void verifyGenericArgCounts(GenericVerifier verifier) throws CompilationException {
        //do nothing
    }

    @Override
    public TypedExpr infer(TypeChecker checker, List<Type> typeGenerics) throws CompilationException {
        return new TypedVariable(loc, name, checker.lookup(loc, name));
    }

    @Override
    public TypedExpr check(TypeChecker checker, List<Type> typeGenerics, Type expected) throws CompilationException {
        TypedExpr e = infer(checker, typeGenerics);
        if (!e.type().isSubtype(expected, checker.pool()))
            throw new TypeCheckingException("Expected " + expected.name(checker.pool()) + ", got " + e.type().name(checker.pool()), loc);
        return e;
    }
}
