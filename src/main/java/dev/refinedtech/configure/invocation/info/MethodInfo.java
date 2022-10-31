package dev.refinedtech.configure.invocation.info;

import dev.refinedtech.configure.utils.ReflectionUtils;
import dev.refinedtech.configure.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class MethodInfo {

    public enum MethodType {
        GETTER("get"),
        SETTER("set");

        private final String name;

        MethodType(String name) {
            this.name = name.toLowerCase(Locale.ROOT);
        }

        public final String extractName(String name) {
            if (this.matches(name)) {
                return StringUtils.startingLowercase(name.substring(this.name.length()));
            }

            return name;
        }

        public final boolean matches(String name) {
            return name.toLowerCase(Locale.ROOT).startsWith(this.name);
        }

        public static MethodType getType(String name) {
            for (MethodType type : values()) {
                if (type.matches(name)) {
                    return type;
                }
            }

            return null;
        }
    }

    private final MethodType type;
    private final String name;
    private final List<String> path;
    private final String fullPath;

    private MethodInfo(MethodType type, String name, List<String> path) {
        this.type = type;
        this.name = type.extractName(name);
        this.path = path;

        this.fullPath = String.join(".", path) + "." + name;
    }

    public static MethodInfo from(Method method) {
        String methodName = method.getName();
        MethodType type = MethodType.getType(methodName);
        if (type == null) {
            throw new IllegalStateException("Method '%s' has no associated type.".formatted(methodName));
        }

        List<String> names = ReflectionUtils.findRootInterface(method)
                                            .stream()
                                            .map(Class::getSimpleName)
                                            .map(StringUtils::startingLowercase)
                                            .sorted(Collections.reverseOrder())
                                            .toList();

        return new MethodInfo(type, methodName, names);
    }

    public MethodType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getPath() {
        return path;
    }

    public String getFullPath() {
        return fullPath;
    }
}
