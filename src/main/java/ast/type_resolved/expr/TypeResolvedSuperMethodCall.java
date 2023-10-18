package ast.type_resolved.expr;

import ast.passes.GenericVerifier;
import ast.passes.TypeChecker;
import ast.type_resolved.ResolvedType;
import ast.typed.Type;
import ast.typed.def.method.MethodDef;
import ast.typed.expr.TypedExpr;
import ast.typed.expr.TypedSuperMethodCall;
import exceptions.CompilationException;
import exceptions.GenericCountException;
import exceptions.ParsingException;
import lexing.Loc;

import java.util.List;

public record TypeResolvedSuperMethodCall(Loc loc, int depth, String methodName, List<ResolvedType> genericArgs, List<TypeResolvedExpr> args) implements TypeResolvedExpr {

    @Override
    public void verifyGenericArgCounts(GenericVerifier verifier) throws CompilationException {
        if (genericArgs.size() > 0 && methodName.equals("new"))
            throw new GenericCountException("super() initializer calls cannot have generic args", loc);
        for (var genericArg : genericArgs)
            verifier.verifyType(genericArg, loc);
        for (var arg : args)
            arg.verifyGenericArgCounts(verifier);
    }

    @Override
    public TypedExpr infer(Type currentType, TypeChecker checker, List<Type> typeGenerics) throws CompilationException {
        if (currentType == null)
            throw new ParsingException("Attempt to use super outside of any type definition", loc);
        //Lookup best method
        TypeChecker.BestMethodInfo bestMethod = checker.getBestMethod(loc, currentType, currentType, methodName, args, genericArgs, typeGenerics, false, null, depth);
        Type actualReceiverType = bestMethod.receiverType();
        MethodDef matchingMethod = bestMethod.methodDef();
        List<TypedExpr> typedArgs = bestMethod.typedArgs();
        //Create typed call
        return new TypedSuperMethodCall(loc, actualReceiverType, matchingMethod, typedArgs, matchingMethod.returnType());
    }

    @Override
    public TypedExpr check(Type currentType, TypeChecker checker, List<Type> typeGenerics, Type expected) throws CompilationException {
        //Almost identical, but passes an expected return type.
        if (currentType == null)
            throw new ParsingException("Attempt to use super outside of any type definition", loc);
        //Lookup best method
        TypeChecker.BestMethodInfo bestMethod = checker.getBestMethod(loc, currentType, currentType, methodName, args, genericArgs, typeGenerics, false, expected, depth);
        Type actualReceiverType = bestMethod.receiverType();
        MethodDef matchingMethod = bestMethod.methodDef();
        List<TypedExpr> typedArgs = bestMethod.typedArgs();
        //Create typed call
        return new TypedSuperMethodCall(loc, actualReceiverType, matchingMethod, typedArgs, matchingMethod.returnType());
    }
}