package net.azisaba.tabber.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Represents a logger.
 */
public interface Logger {
    @NotNull Logger DEFAULT_LOGGER = createFromJavaLogger(java.util.logging.Logger.getLogger("AziPluginMessaging"));

    void info(@NotNull String message);
    void info(@NotNull String message, Object p1);
    void info(@NotNull String message, Object p1, Object p2);
    void info(@NotNull String message, Object... params);
    void info(@NotNull String message, Throwable throwable);
    void warn(@NotNull String message);
    void warn(@NotNull String message, Object p1);
    void warn(@NotNull String message, Object p1, Object p2);
    void warn(@NotNull String message, Object... params);
    void warn(@NotNull String message, Throwable throwable);
    void error(@NotNull String message);
    void error(@NotNull String message, Object p1);
    void error(@NotNull String message, Object p1, Object p2);
    void error(@NotNull String message, Object... params);
    void error(@NotNull String message, Throwable throwable);

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Logger createByProxy(@NotNull Object instance) {
        Objects.requireNonNull(instance, "instance cannot be null");
        Class<?> instClass = instance.getClass();
        return (Logger) Proxy.newProxyInstance(Logger.class.getClassLoader(), new Class[] { Logger.class }, (proxy, method, args) -> {
            Method m = findMethod(instClass, method);
            if (m == null) throw new RuntimeException(instClass.getTypeName() + " does not implements " + method.toGenericString());
            return m.invoke(instance, args);
        });
    }

    @Nullable
    private static Method findMethod(@NotNull Class<?> clazz, @NotNull Method m) {
        try {
            return clazz.getDeclaredMethod(m.getName(), m.getParameterTypes());
        } catch (ReflectiveOperationException ignore) {}
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            Method method = findMethod(superClass, m);
            if (method != null) return method;
        }
        for (Class<?> c : clazz.getInterfaces()) {
            Method method = findMethod(c, m);
            if (method != null) return method;
        }
        return null;
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Logger createFromJavaLogger(@NotNull java.util.logging.Logger logger) {
        return new Logger() {
            @NotNull
            private String format(@NotNull String msg) {
                int length = 0;
                while (msg.contains("{}")) {
                    msg = msg.replaceFirst("\\{}", "{" + length++ + "}");
                }
                return msg;
            }

            @Override
            public void info(@NotNull String message) {
                logger.log(Level.INFO, format(message));
            }

            @Override
            public void info(@NotNull String message, Object p1) {
                logger.log(Level.INFO, format(message), p1);
            }

            @Override
            public void info(@NotNull String message, Object p1, Object p2) {
                logger.log(Level.INFO, format(message), new Object[]{p1, p2});
            }

            @Override
            public void info(@NotNull String message, Object... params) {
                logger.log(Level.INFO, format(message), params);
            }

            @Override
            public void info(@NotNull String message, Throwable throwable) {
                logger.log(Level.INFO, format(message), throwable);
            }

            @Override
            public void warn(@NotNull String message) {
                logger.log(Level.WARNING, format(message));
            }

            @Override
            public void warn(@NotNull String message, Object p1) {
                logger.log(Level.WARNING, format(message), p1);
            }

            @Override
            public void warn(@NotNull String message, Object p1, Object p2) {
                logger.log(Level.WARNING, format(message), new Object[]{p1, p2});
            }

            @Override
            public void warn(@NotNull String message, Object... params) {
                logger.log(Level.WARNING, format(message), params);
            }

            @Override
            public void warn(@NotNull String message, Throwable throwable) {
                logger.log(Level.WARNING, format(message), throwable);
            }

            @Override
            public void error(@NotNull String message) {
                logger.log(Level.SEVERE, format(message));
            }

            @Override
            public void error(@NotNull String message, Object p1) {
                logger.log(Level.SEVERE, format(message), p1);
            }

            @Override
            public void error(@NotNull String message, Object p1, Object p2) {
                logger.log(Level.SEVERE, format(message), new Object[]{p1, p2});
            }

            @Override
            public void error(@NotNull String message, Object... params) {
                logger.log(Level.SEVERE, format(message), params);
            }

            @Override
            public void error(@NotNull String message, Throwable throwable) {
                logger.log(Level.SEVERE, format(message), throwable);
            }
        };
    }

    /**
     * Returns the logger on the current environment. Returns a default one if the plugin is not yet enabled.
     * @return the logger
     */
    static @NotNull Logger getCurrentLogger() {
        try {
            return TabberProvider.get().getLogger();
        } catch (IllegalStateException ignore) {}
        return DEFAULT_LOGGER;
    }
}
