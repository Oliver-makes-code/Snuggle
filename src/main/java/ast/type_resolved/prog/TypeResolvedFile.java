package ast.type_resolved.prog;

import ast.passes.TypeChecker;
import ast.type_resolved.ResolvedType;
import ast.type_resolved.expr.TypeResolvedExpr;
import ast.type_resolved.expr.TypeResolvedImport;
import ast.typed.prog.TypedFile;
import exceptions.compile_time.CompilationException;
import util.LateInit;
import util.ListUtils;

import java.util.List;

public record TypeResolvedFile(String name, List<TypeResolvedImport> imports, List<ResolvedType.Basic> types, List<TypeResolvedExpr> code) {

    public TypedFile type(TypeChecker checker) throws CompilationException {
        return new TypedFile(
                name,
                ListUtils.map(imports, i -> i.infer(null, checker, List.of())),
                new LateInit<>(() -> ListUtils.flatten(ListUtils.map(types, checker::getAllInstantiated))),
                ListUtils.map(code, e -> e.infer(null, checker, List.of()))
        );
    }

}
