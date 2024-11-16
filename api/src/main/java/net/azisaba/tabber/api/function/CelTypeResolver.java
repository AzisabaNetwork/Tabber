package net.azisaba.tabber.api.function;

import dev.cel.common.types.CelType;
import dev.cel.common.types.CelTypes;
import dev.cel.common.types.ListType;
import dev.cel.common.types.SimpleType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CelTypeResolver {
    private static final Map<Class<?>, CelType> CEL_TYPE_MAP = new HashMap<>(Map.ofEntries(
            Map.entry(String.class, SimpleType.STRING),
            Map.entry(Integer.class, SimpleType.INT),
            Map.entry(int.class, SimpleType.INT),
            Map.entry(Long.class, SimpleType.UINT),
            Map.entry(long.class, SimpleType.UINT),
            Map.entry(Double.class, SimpleType.DOUBLE),
            Map.entry(double.class, SimpleType.DOUBLE),
            Map.entry(Float.class, SimpleType.DOUBLE),
            Map.entry(float.class, SimpleType.DOUBLE),
            Map.entry(Boolean.class, SimpleType.BOOL),
            Map.entry(boolean.class, SimpleType.BOOL),
            Map.entry(Void.class, SimpleType.NULL_TYPE),
            Map.entry(void.class, SimpleType.NULL_TYPE),
            Map.entry(Object.class, SimpleType.ANY),
            Map.entry(byte[].class, SimpleType.BYTES),
            Map.entry(List.class, ListType.create(SimpleType.DYN)),
            Map.entry(Collection.class, ListType.create(SimpleType.DYN))
    ));

    public static @NotNull CelType resolve(@NotNull Class<?> clazz) {
        if (CEL_TYPE_MAP.containsKey(clazz)) {
            return CEL_TYPE_MAP.get(clazz);
        }
        CelType type = CelTypes.typeToCelType(CelTypes.createMessage(clazz.getSimpleName()));
        CEL_TYPE_MAP.put(clazz, type);
        return type;
    }

    public static void addType(@NotNull Class<?> clazz, @NotNull CelType type) {
        CEL_TYPE_MAP.put(clazz, type);
    }

    private CelTypeResolver() {
        throw new UnsupportedOperationException();
    }
}
