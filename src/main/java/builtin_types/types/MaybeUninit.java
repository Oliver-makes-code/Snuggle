package builtin_types.types;

import ast.passes.TypeChecker;
import ast.typed.def.field.BuiltinFieldDef;
import ast.typed.def.field.FieldDef;
import ast.typed.def.field.SnuggleFieldDef;
import ast.typed.def.method.BytecodeMethodDef;
import ast.typed.def.method.MethodDef;
import ast.typed.def.type.IndirectTypeDef;
import ast.typed.def.type.StructDef;
import ast.typed.def.type.TypeDef;
import builtin_types.BuiltinType;
import org.objectweb.asm.Opcodes;
import util.ListUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Used for data structures, like Array<T> where T is a reference type or contains a reference type
public class MaybeUninit implements BuiltinType {

    public static final MaybeUninit INSTANCE = new MaybeUninit();
    private MaybeUninit() {}

    @Override
    public List<FieldDef> getFields(TypeChecker checker, List<TypeDef> generics) {
        TypeDef thisType = checker.getGenericBuiltin(INSTANCE, generics);
        TypeDef innerType = generics.get(0);
        if (innerType.get().isPlural()) {
            return ListUtils.map(ListUtils.filter(
                    innerType.fields(),
                    f -> !f.isStatic()),
                    f -> new BuiltinFieldDef(
                            f.name(),
                            thisType,
                            checker.getGenericBuiltin(INSTANCE, List.of(f.type())),
                            false
                    )
            );
        } else {
            if (innerType.isReferenceType())
                return List.of(new BuiltinFieldDef(innerType.name(), thisType, checker.getGenericBuiltin(OptionType.INSTANCE, List.of(innerType)), false));
            else
                return List.of(new BuiltinFieldDef(innerType.name(), thisType, innerType, false));
        }
    }

    @Override
    public List<MethodDef> getMethods(TypeChecker checker, List<TypeDef> generics) {
        TypeDef thisType = checker.getGenericBuiltin(INSTANCE, generics);
        TypeDef innerType = generics.get(0);
        TypeDef unitType = checker.getBasicBuiltin(UnitType.INSTANCE);
        return List.of(
                new BytecodeMethodDef("new", false, thisType, List.of(innerType), unitType, true, v -> {
                    //Wraps the argument into one of these
                    //No-op! This stuff is unsafe, it literally just changes the type
                }),
                new BytecodeMethodDef("get", false, thisType, List.of(), innerType, true, v -> {
                    //No-op! This is unsafe, it literally just changes the type. Worst that can happen is
                })
        );
    }

    @Override
    public String name() {
        return "MaybeUninit";
    }

    @Override
    public List<String> descriptor(TypeChecker checker, List<TypeDef> generics) {
        return generics.get(0).getDescriptor();
    }

    @Override
    public String runtimeName(TypeChecker checker, List<TypeDef> generics) {
        return generics.get(0).runtimeName();
    }

    @Override
    public String returnDescriptor(TypeChecker checker, List<TypeDef> generics) {
        return generics.get(0).getReturnTypeDescriptor();
    }

    @Override
    public boolean isReferenceType(TypeChecker checker, List<TypeDef> generics) {
        return false;
    }

    @Override
    public boolean isPlural(TypeChecker checker, List<TypeDef> generics) {
        return true;
    }

    @Override
    public boolean hasSpecialConstructor(TypeChecker checker, List<TypeDef> generics) {
        return true;
    }

    @Override
    public int numGenerics() {
        return 1;
    }

    @Override
    public boolean extensible(TypeChecker checker, List<TypeDef> generics) {
        return false;
    }

    @Override
    public int stackSlots(TypeChecker checker, List<TypeDef> generics) {
        return generics.get(0).stackSlots();
    }


}
