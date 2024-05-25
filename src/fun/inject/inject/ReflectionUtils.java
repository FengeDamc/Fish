package fun.inject.inject;


import fun.inject.Agent;


import java.lang.reflect.*;
import java.util.Set;

public class ReflectionUtils {

    public static Object getFieldValue(Object instance, String name) {
        Class<?> c = instance.getClass();
        while (c.getSuperclass() != null) {

            try {
                Field field;
                try {
                    field = c.getDeclaredField(name);
                } catch (NoSuchFieldException | SecurityException e) {
                    field = c.getField(name);
                }
                field.setAccessible(true);
                return field.get(instance);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            c = c.getSuperclass();
        }
        Agent.logger.error("cant find {} {}",instance.getClass().getName(),name);
        return null;
    }

    public static Object getFieldValue(Class<?> clazz, String name) {
        Class<?> c = clazz;
        while (c.getSuperclass() != null) {
            try {
                Field field;
                try {
                    field = c.getDeclaredField(name);
                } catch (NoSuchFieldException | SecurityException e) {
                    field = c.getField(name);
                }
                field.setAccessible(true);
                return field.get(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            c = c.getSuperclass();
        }

        return null;
    }

    public static void setFieldValue(Object instance, String name, Object value) {
        Class<?> c = instance.getClass();
        while (c.getSuperclass() != null) {
            try {
                Field field;
                try {
                    field = c.getDeclaredField(name);
                } catch (NoSuchFieldException | SecurityException e) {
                    field = c.getField(name);
                }
                field.setAccessible(true);
                /*Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);*/
                field.set(instance, value);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                ignored.printStackTrace();
            }

            c = c.getSuperclass();
        }
    }

    public static Object invokeMethod(Object instance, String name, Class<?>[] desc, Object... args) {
        Class<?> c = instance.getClass();
        while (c.getSuperclass() != null) {
            try {
                Method method;
                try {
                    method = c.getDeclaredMethod(name, desc);
                } catch (NoSuchMethodException | SecurityException exception) {
                    method = c.getMethod(name, desc);
                }
                method.setAccessible(true);
                return method.invoke(instance, args);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }

            c = c.getSuperclass();
        }
        return null;
    }

    public static Object invokeMethod(Class<?> clazz, String name, Class<?>[] desc, Object... args) {
        Class<?> c = clazz;
        while (c.getSuperclass() != null) {
            try {
                Method method;
                try {
                    method = c.getDeclaredMethod(name, desc);
                } catch (NoSuchMethodException | SecurityException exception) {
                    method = c.getMethod(name, desc);
                }
                method.setAccessible(true);
                return method.invoke(null, args);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }

            c = c.getSuperclass();
        }
        return null;
    }

    public static Object invokeMethod(Object instance, String name) {
        Class<?> c = instance.getClass();
        while (c.getSuperclass() != null) {
            try {
                Method method;
                try {
                    method = c.getDeclaredMethod(name);
                } catch (NoSuchMethodException | SecurityException exception) {
                    //exception.printStackTrace();
                    method = c.getMethod(name);
                }
                method.setAccessible(true);
                return method.invoke(instance);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                //ignored.printStackTrace();
            }

            c = c.getSuperclass();
        }
        Agent.logger.error("cant find {} {}",instance.getClass().getName(),name);

        return null;
    }

    public static Object invokeMethod(Class<?> clazz, String name) {
        Class<?> c = clazz;
        while (c.getSuperclass() != null) {
            try {
                Method method;
                try {
                    method = c.getDeclaredMethod(name);
                } catch (NoSuchMethodException | SecurityException exception) {
                    method = c.getMethod(name);
                }
                method.setAccessible(true);
                return method.invoke(null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }

            c = c.getSuperclass();
        }
        return null;
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static Object newInstance(Class<?> clazz, Class<?>[] desc, Object... args) {
        Class<?> c = clazz;
        while (c.getSuperclass() != null) {
            try {
                Constructor<?> constructor;
                try {
                    constructor = c.getDeclaredConstructor(desc);
                } catch (NoSuchMethodException | SecurityException exception) {
                    constructor = c.getConstructor(desc);
                }
                constructor.setAccessible(true);
                return constructor.newInstance(args);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                     InstantiationException ignored) {
            }

            c = c.getSuperclass();
        }
        return null;
    }


    public static boolean isInstanceOf(Object obj, Class<?> cls) {
        return cls.isAssignableFrom(obj.getClass());
    }
}
