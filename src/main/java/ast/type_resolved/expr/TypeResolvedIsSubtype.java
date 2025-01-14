package ast.type_resolved.expr;

import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.type_resolved.ResolvedType;
import ast.type_resolved.def.field.TypeResolvedFieldDef;
import ast.type_resolved.def.type.TypeResolvedTypeDef;
import ast.typed.def.type.TypeDef;
import ast.typed.expr.TypedExpr;
import ast.typed.expr.TypedLiteral;
import builtin_types.types.BoolType;
import exceptions.compile_time.CompilationException;
import exceptions.compile_time.TypeCheckingException;
import lexing.Loc;

import java.util.List;

//No need for a Typed version, since this always infers/checks into a TypedLiteral
public record TypeResolvedIsSubtype(Loc loc, ResolvedType type1, ResolvedType type2) implements TypeResolvedExpr {
    @Override
    public void verifyGenericArgCounts(GenericVerifier verifier) throws CompilationException {
        verifier.verifyType(type1, loc);
        verifier.verifyType(type2, loc);
    }

    @Override
    public TypedExpr infer(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics) throws CompilationException {
        TypeDef type1Def = checker.getOrInstantiate(type1, typeGenerics);
        TypeDef type2Def = checker.getOrInstantiate(type2, typeGenerics);
        return new TypedLiteral(loc, type1Def.isSubtype(type2Def), checker.getBasicBuiltin(BoolType.INSTANCE));
    }

    @Override
    public TypedExpr check(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics, TypeDef expected) throws CompilationException {
        TypedExpr res = infer(currentType, checker, typeGenerics);
        if (!res.type().isSubtype(expected))
            throw new TypeCheckingException("Expected type \"" + expected.name() + "\", but got type \"bool\"", loc);
        return res;
    }
}
