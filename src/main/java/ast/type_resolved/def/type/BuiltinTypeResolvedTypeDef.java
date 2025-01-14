package ast.type_resolved.def.type;

import ast.typed.def.type.BuiltinTypeDef;
import exceptions.compile_time.CompilationException;
import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.typed.def.type.TypeDef;
import builtin_types.BuiltinType;
import util.LateInit;

import java.util.List;

public record BuiltinTypeResolvedTypeDef(BuiltinType builtin) implements TypeResolvedTypeDef {

    @Override
    public String name() {
        return builtin.name();
    }

    @Override
    public int numGenerics() {
        return builtin.numGenerics();
    }

    @Override
    public void verifyGenericCounts(GenericVerifier verifier) throws CompilationException {}

    @Override
    public TypeDef instantiate(TypeDef currentType, TypeChecker checker, List<TypeDef> generics) {
        return new BuiltinTypeDef(builtin, generics, checker);
    }

}
