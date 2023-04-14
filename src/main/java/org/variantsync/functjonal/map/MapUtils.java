package org.variantsync.functjonal.map;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapUtils {
    public static <K1, K2, V> Map<K2, V> TransKeys(
            Map<K1, V> m,
            Function<K1, K2> f,
            Supplier<Map<K2, V>> newMap)
    {
        final Map<K2, V> result = newMap.get();
        for (Map.Entry<K1, V> entry : m.entrySet()) {
            if (result.put(f.apply(entry.getKey()), entry.getValue()) != null) {
                throw new IllegalArgumentException(
                        "Key collision at key "
                                + entry.getKey()
                                + "! Given function is not injective!");
            }
        }

        return result;
    }
    public static <K1, K2, V> Map<K2, V> TransKeys(
            Map<K1, V> m,
            Map<K1, K2> f,
            Supplier<Map<K2, V>> newMap)
    {
        return TransKeys(
                m,
                f::get,
                newMap
        );
    }
}
