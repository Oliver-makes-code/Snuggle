package builtin_types.types;

import ast.passes.TypePool;
import ast.typed.Type;
import ast.typed.def.method.BytecodeMethodDef;
import ast.typed.def.method.MethodDef;
import builtin_types.BuiltinType;
import exceptions.compile_time.CompilationException;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class ObjType implements BuiltinType {

    public static final ObjType INSTANCE = new ObjType();
    private ObjType() {}

    @Override
    public String name() {
        return "Obj";
    }

    @Override
    public String getDescriptor(List<Type> generics, TypePool pool) {
        return "Ljava/lang/Object;";
    }

    @Override
    public String getRuntimeName(List<Type> generics, TypePool pool) {
        return "java/lang/Object";
    }

    @Override
    public boolean extensible() {
        return true;
    }

    @Override
    public List<? extends MethodDef> getMethods(List<Type> generics, TypePool pool) throws CompilationException {
        Type thisType = pool.getBasicBuiltin(INSTANCE);
        Type unitType = pool.getBasicBuiltin(UnitType.INSTANCE);
        return List.of(
                new BytecodeMethodDef(false, "new", List.of(), unitType, v ->
                        v.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false))
        );
    }

    @Override
    public boolean isReferenceType(List<Type> generics, TypePool pool) {
        return true;
    }
}
