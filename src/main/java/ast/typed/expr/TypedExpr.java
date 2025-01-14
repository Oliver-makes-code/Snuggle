package ast.typed.expr;

import ast.ir.def.CodeBlock;
import ast.typed.def.field.FieldDef;
import ast.typed.def.type.TypeDef;
import exceptions.compile_time.CompilationException;
import lexing.Loc;

public interface TypedExpr {

    //Still know the location
    Loc loc();

    //A TypedExpr should know its TypeDef once created.
    TypeDef type();

    //If the output type of this expr is plural, then desiredFields indicates which fields
    //in particular we want to grab.
    void compile(CodeBlock block, DesiredFieldNode desiredFields) throws CompilationException;

    //Compiles and then pops the result off the stack
//    default void compileAndPop(Compiler compiler, ScopeHelper env, MethodVisitor visitor) throws CompilationException {
//        compile(compiler, env, visitor);
//        TypeDef def = compiler.getTypeDef(type());
//        BytecodeHelper.pop(def, visitor);
//    }



}
