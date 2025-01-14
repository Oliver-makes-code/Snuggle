package ast.type_resolved.expr;

import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.type_resolved.ResolvedType;
import ast.type_resolved.def.field.SnuggleTypeResolvedFieldDef;
import ast.type_resolved.def.field.TypeResolvedFieldDef;
import ast.typed.def.field.FieldDef;
import ast.typed.def.field.SnuggleFieldDef;
import ast.typed.def.type.TypeDef;
import ast.typed.expr.TypedExpr;
import ast.typed.expr.TypedFieldAccess;
import exceptions.compile_time.CompilationException;
import exceptions.compile_time.TypeCheckingException;
import exceptions.compile_time.UndeclaredVariableException;
import lexing.Loc;
import util.ListUtils;

import java.util.List;

public record TypeResolvedStaticFieldAccess(Loc loc, ResolvedType type, String fieldName) implements TypeResolvedExpr {

    @Override
    public void verifyGenericArgCounts(GenericVerifier verifier) throws CompilationException {
        verifier.verifyType(type, loc);
    }

    @Override
    public TypedExpr infer(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics) throws CompilationException {
        TypeDef typeDef = checker.getOrInstantiate(type, typeGenerics);

        FieldDef fieldDef = ListUtils.find(typeDef.fields(), f -> {
            if (!f.isStatic()) return false;
            if (!f.name().equals(fieldName)) return false;
            if (f.pub()) return true;
            if (f instanceof SnuggleFieldDef sf)
                return sf.loc().fileName().equals(loc.fileName());
            throw new IllegalStateException("Non-snuggle field defs should always be pub? Bug in compiler, please report");
        });
        if (fieldDef == null)
            throw new UndeclaredVariableException("Unable to locate static field \"" + fieldName + "\" on type " + typeDef.name(), loc);
        return new TypedFieldAccess(loc, null, fieldDef, fieldDef.type());
    }

    @Override
    public TypedExpr check(TypeDef currentType, TypeChecker checker, List<TypeDef> typeGenerics, TypeDef expected) throws CompilationException {
        TypedExpr e = infer(currentType, checker, typeGenerics);
        if (!e.type().isSubtype(expected))
            throw new TypeCheckingException("Expected type " + expected.name() + ", but static field \"" + fieldName + "\" has type " + e.type().name(), loc);
        return e;
    }
}
