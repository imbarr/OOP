package serializator;
import javafx.util.Pair;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Serializator {
    public Serializator(String usedPackage) {
        this.usedPackage = usedPackage;
    }

    public final String usedPackage;

    public static final String preamble = "<serialized>";
    public static final char screeningSymbol = '#';
    public static final String service = "()|," + screeningSymbol;

    public byte[] serialize(Object object) {
        return (preamble + object.getClass().getSimpleName() + '(' + allFieldsToString(object) + ')')
                .getBytes(StandardCharsets.UTF_8);
    }

    private List<Field> getAllFields(Object object) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = object.getClass(); c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    private Pair<Types, String> fromArray(Field field, Object object) {
        if(!field.getType().isArray())
            return null;
        int length = Array.getLength(object);
        Pair<Types, Function<Integer, String>> pair = getTypeAndGetter(field, object);
        Function<Integer, String> toString = pair.getValue();
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < length; i++) {
            result.append(toString.apply(i)).append(",");
        }
        return new Pair<>(pair.getKey(), result.toString());
    }

    private Pair<Types, Function<Integer, String>> getTypeAndGetter(Field field, Object array) {
        Class<?> type = field.getType().getComponentType();
        if(byte.class.equals(type))
            return new Pair<>(Types.Byte, i -> Byte.toString(Array.getByte(array, i)));
        if(short.class.equals(type))
            return new Pair<>(Types.Short, i -> Short.toString(Array.getShort(array, i)));
        if(int.class.equals(type))
            return new Pair<>(Types.Integer, i -> Integer.toString(Array.getInt(array, i)));
        if(long.class.equals(type))
            return new Pair<>(Types.Long, i -> Long.toString(Array.getLong(array, i)));
        if(float.class.equals(type))
            return new Pair<>(Types.Float, i -> Float.toString(Array.getFloat(array, i)));
        if(double.class.equals(type))
            return new Pair<>(Types.Double, i -> Double.toString(Array.getDouble(array, i)));
        if(char.class.equals(type))
            return new Pair<>(Types.Character, i -> Character.toString(Array.getChar(array, i)));
        if(boolean.class.equals(type))
            return new Pair<>(Types.Boolean, i -> Boolean.toString(Array.getBoolean(array, i)));
        return new Pair<>(null, i ->
                "(" + Array.get(array, i).getClass().getSimpleName() +
                        "|" + allFieldsToString(Array.get(array, i)) + ")");
    }

    private Pair<Types, String> fromPrimitiveOrSpecial(Field field, Object object){
        try {
            Class<?> type = field.getType();
            if (byte.class.equals(type))
                return new Pair<>(Types.Byte, Byte.toString(field.getByte(object)));
            if (short.class.equals(type))
                return new Pair<>(Types.Short, Short.toString(field.getShort(object)));
            if (int.class.equals(type))
                return new Pair<>(Types.Integer, Integer.toString(field.getInt(object)));
            if (long.class.equals(type))
                return new Pair<>(Types.Long, Long.toString(field.getLong(object)));
            if (float.class.equals(type))
                return new Pair<>(Types.Float, Float.toString(field.getFloat(object)));
            if (double.class.equals(type))
                return new Pair<>(Types.Double, Double.toString(field.getDouble(object)));
            if (char.class.equals(type))
                return new Pair<>(Types.Character, Character.toString(field.getChar(object)));
            if (boolean.class.equals(type))
                return new Pair<>(Types.Boolean, Boolean.toString(field.getBoolean(object)));
            if (String.class.equals(type) && field.get(object) != null)
                return new Pair<>(Types.String, screen((String)field.get(object)));
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    private String fieldToString(Field field, Object object) {
        field.setAccessible(true);
        Pair<Types, String> special = fromPrimitiveOrSpecial(field, object);
        if(special != null)
            return "(" + field.getName() + "|p|" + special.getKey() + "|" + special.getValue() + ")";
        try {
            Object next = field.get(object);
            if(next != null) {
                Pair<Types, String> array = fromArray(field, next);
                if (array != null)
                    return "(" + field.getName() + (array.getKey() == null ? "|a|" : "|pa|")
                            + Array.getLength(next) + "|" +
                            (array.getKey() == null
                                    ? field.getType().getComponentType().getSimpleName()
                                    : array.getKey()) +
                            "|" + array.getValue() + ")";
                return "(" + field.getName() + "|o|" + field.getDeclaringClass().getSimpleName() + "|" +
                        allFieldsToString(next) + ")";
            }
        } catch (IllegalAccessException ignored) {}
        return "";
    }

    private String allFieldsToString(Object object) {
        return getAllFields(object).stream()
                .map(f -> fieldToString(f, object))
                .collect(Collectors.joining(""));
    }

    private String screen(String string) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < string.length(); i++) {
            char current = string.charAt(i);
            if(service.indexOf(current) != -1)
                sb.append('#');
            sb.append(current);
        }
        return sb.toString();
    }

    public Object deserialize(byte[] raw) throws ParseException {
        return wrapExceptions(() -> {
            StringReader reader = new StringReader(new String(raw, StandardCharsets.UTF_8));
            String start = readUntil(reader, "(");
            if (!start.startsWith(preamble))
                throw new ParseException("Data does not start with preamble");
            Object result = Class.forName(usedPackage + "." + start.substring(preamble.length(), start.length()))
                    .getConstructor().newInstance();
            readFields(reader, result);
            return result;
        });
    }

    private void readFields(StringReader reader, Object object) throws ParseException {
        while (true) {
            int next = safeRead(reader);
            if(next == -1)
                throw new ParseException("Unexpected end of stream");
            if((char)next == '(')
                readField(reader, object);
            else if((char)next == ')')
                return;
            else
                throw new ParseException("Unexpected character");
        }
    }

    private void readField(StringReader reader, Object object) throws ParseException {
        wrapExceptions(() -> {
            Field field = object.getClass().getField(readUntil(reader, "|"));
            field.setAccessible(true);
            String category = readUntil(reader, "|");

            if (category.equals("o")) {
                String className = readUntil(reader, "|");
                Constructor<?> constructor = Class.forName(usedPackage + "." + className).getConstructor();
                constructor.setAccessible(true);
                Object n = constructor.newInstance();
                readFields(reader, n);
                try {
                    field.set(object, n);
                } catch (IllegalArgumentException e) {
                    throw new ParseException("Field has a different type", e);
                }
            } else if (category.equals("p")) {
                String className = readUntil(reader, "|");
                setPrimitiveOrSpecial(readUntil(reader, ")"), Types.valueOf(className), field, object);
            } else if (category.equals("a") || category.equals("pa")) {
                int length;
                try {
                    length = Integer.parseInt(readUntil(reader, "|"));
                } catch (NumberFormatException e) {
                    throw new ParseException("Invalid array length.");
                }
                String className = readUntil(reader, "|");
                Object array = Array.newInstance(Class.forName(usedPackage + "." + className), length);
                readElements(reader, array, category.equals("pa") ? Types.valueOf(className) : null, length);
                field.set(object, array);
            }
            return null;
        });
    }

    private void readElements(StringReader reader, Object array, Types type, int length) throws ParseException {
        try {
            for(int i = 0; i < length; i++)
                if (type == null) {
                    Array.set(array, i, readObjectInArray(reader));
                    safeRead(reader);
                }
                else if (type == Types.Byte)
                        Array.setByte(array, i, Byte.valueOf(readUntil(reader, ",)")));
                else if (type == Types.Short)
                        Array.setShort(array, i, Short.valueOf(readUntil(reader, ",)")));
                else if (type == Types.Integer)
                        Array.setInt(array, i, Integer.valueOf(readUntil(reader, ",)")));
                else if (type == Types.Long)
                        Array.setLong(array, i, Long.valueOf(readUntil(reader, ",)")));
                else if (type == Types.Float)
                        Array.setFloat(array, i, Float.valueOf(readUntil(reader, ",)")));
                else if (type == Types.Double)
                        Array.setDouble(array, i, Double.valueOf(readUntil(reader, ",)")));
                else if (type == Types.Character)
                        Array.setChar(array, i, charValueOf(readUntil(reader, ",)")));
                else if (type == Types.Boolean)
                        Array.setBoolean(array, i, booleanValueOf(readUntil(reader, ",)")));
                else if (type == Types.String)
                        Array.set(array, i, readUntil(reader, ",)"));
        } catch (NumberFormatException e) {
            throw new ParseException("Failed parsing to " + type.toString(), e);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Field has a different type", e);
        }
    }

    private Object readObjectInArray(StringReader reader) throws ParseException {
        return wrapExceptions(() -> {
            int c = safeRead(reader);
            if (c == -1)
                throw new ParseException("Unexpected end of stream");
            if (c != '(')
                throw new ParseException("Not a object");
            Constructor<?> constructor = Class
                    .forName(usedPackage + "." + readUntil(reader, "|"))
                    .getConstructor();
            constructor.setAccessible(true);
            Object object = constructor.newInstance();
            readFields(reader, object);
            return object;
        });
    }

    private void setPrimitiveOrSpecial(String value, Types type, Field field, Object object)
            throws ParseException, IllegalAccessException {
        try {
            if (type == Types.Byte)
                field.setByte(object, Byte.valueOf(value));
            if (type == Types.Short)
                field.setShort(object, Short.valueOf(value));
            if (type == Types.Integer)
                field.setInt(object, Integer.valueOf(value));
            if (type == Types.Long)
                field.setLong(object, Long.valueOf(value));
            if (type == Types.Float)
                field.setFloat(object, Float.valueOf(value));
            if (type == Types.Double)
                field.setDouble(object, Double.valueOf(value));
            if (type == Types.Character)
                field.setChar(object, charValueOf(value));
            if (type == Types.Boolean)
                field.setBoolean(object, booleanValueOf(value));
            if (type == Types.String)
                field.set(object, value);
        } catch (NumberFormatException e) {
            throw new ParseException("Failed parsing to " + type.toString(), e);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Field has a different type", e);
        }
    }

    private char charValueOf(String s) {
        if(s.length() != 1)
            throw new NumberFormatException();
        return s.charAt(0);
    }

    private boolean booleanValueOf(String s) {
        if(s.equals("true"))
            return true;
        else if(s.equals("false"))
            return false;
        throw new NumberFormatException();
    }

    private String readUntil(StringReader reader, String symbols) throws ParseException {
        StringBuilder sb = new StringBuilder();
        boolean screen = false;
        while (true) {
            int next = safeRead(reader);
            if (next == -1)
                throw new ParseException("Unexpected end of stream");
            char current = (char) next;

            if (screen)
                if (service.indexOf(current) != -1) {
                    sb.append(current);
                    screen = false;
                } else
                    throw new ParseException("Invalid screening");
            else if (symbols.indexOf(current) != -1)
                return sb.toString();
            else if (current == screeningSymbol)
                screen = true;
            else if (service.indexOf(current) == -1)
                sb.append(current);
            else
                throw new ParseException("Unexpected service symbol");
        }
    }

    private int safeRead(StringReader reader) {
        try {
            return reader.read();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private <R> R wrapExceptions(CheckedFunction<R> f) throws ParseException {
        try {
            return f.run();
        } catch (NoSuchFieldException e) {
            throw new ParseException("Field not found: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ParseException("Class not found", e);
        } catch (NoSuchMethodException e) {
            throw new ParseException("Class does not have a constructor without parameters", e);
        } catch (InvocationTargetException e) {
            throw new ParseException("Class constructor threw a exception", e);
        } catch (InstantiationException e) {
            throw new ParseException("Class is abstract", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    @FunctionalInterface
    public interface CheckedFunction<R> {
        R run() throws NoSuchFieldException,
                ClassNotFoundException,
                NoSuchMethodException,
                InvocationTargetException,
                InstantiationException,
                IllegalAccessException,
                ParseException;
    }
}
