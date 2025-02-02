package ast.type_resolved.expr;

import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.type_resolved.def.field.TypeResolvedFieldDef;
import ast.typed.def.type.TypeDef;
import ast.typed.expr.TypedAssignment;
import ast.typed.expr.TypedExpr;
import exceptions.compile_time.CompilationException;
import exceptions.compile_time.TypeCheckingException;
import lexing.Loc;

import java.util.List;

public record TypeResolvedAssignment(Loc loc, TypeResolvedExpr lhs, TypeResolvedExpr rhs) implements TypeResolvedExpr {

    @Override
    public void verifyGenericArgCounts(GenericVerifier verifier) throws CompilationException {
        lhs.verifyGenericArgCounts(verifier);
        rhs.verifyGenericArgCounts(verifier);
    }

    @Override
    public TypedAssignment infer(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics) throws CompilationException {
        TypedExpr typedLhs = lhs.infer(currentType, checker, typeGenerics);
        TypeDef type = typedLhs.type();
        TypedExpr typedRhs = rhs.check(currentType, checker, typeGenerics, type);
        return new TypedAssignment(loc, typedLhs, typedRhs, type);
    }

    @Override
    public TypedExpr check(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics, TypeDef expected) throws CompilationException {
        TypedAssignment inferred = infer(currentType, checker, typeGenerics);
        if (!inferred.type().isSubtype(expected))
            throw new TypeCheckingException("Expected " + expected.name() + ", got " + inferred.type().name(), loc);
        return inferred;
    }
}
