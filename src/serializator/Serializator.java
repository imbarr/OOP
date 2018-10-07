package serializator;
import javafx.util.Pair;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Serializator {
    public static final String usedPackage = "serializator.test_classes";

    public static final String preamble = "<serialized>";
    public static final char screeningSymbol = '#';
    public static final String service = "()|" + screeningSymbol;

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
            if(next != null)
                return "(" + field.getName() + "|o|" + field.getDeclaringClass().getSimpleName() + "|" +
                    allFieldsToString(next) + ")";
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

    public Object deserialize(byte[] raw) throws Exception {
        StringReader reader = new StringReader(new String(raw, StandardCharsets.UTF_8));
        String start = readUntil(reader, '(');
        if(!start.startsWith(preamble))
            throw new IllegalArgumentException("Data does not start with preamble");
        Object result = Class.forName(usedPackage + "." + start.substring(preamble.length(), start.length()))
                .getConstructor().newInstance();
        readFields(reader, result);
        return result;
    }

    private void readFields(StringReader reader, Object object) throws ParseException {
        while (true) {
            int next = safeRead(reader);
            if(next == -1)
                throw new IllegalArgumentException("Unexpected end of stream");
            if((char)next == '(')
                readField(reader, object);
            else if((char)next == ')')
                return;
            else
                throw new IllegalArgumentException("Unexpected character");
        }
    }

    private void readField(StringReader reader, Object object) throws ParseException {
        try {
            Field field = object.getClass().getDeclaredField(readUntil(reader, '|'));
            field.setAccessible(true);
            String category = readUntil(reader, '|');
            String className = readUntil(reader, '|');

            if (category.equals("o")) {
                Constructor<?> constructor = Class.forName(usedPackage + "." + className).getConstructor();
                constructor.setAccessible(true);
                Object n = constructor.newInstance();
                readFields(reader, n);
                try {
                    field.set(object, n);
                } catch (IllegalArgumentException e) {
                    throw new ParseException("Field has a different type", e);
                }
            } else if (category.equals("p"))
                setPrimitiveOrSpecial(readUntil(reader, ')'), Types.valueOf(className), field, object);
        } catch (NoSuchFieldException e) {
            throw new ParseException("Field not found", e);
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

    private String readUntil(StringReader reader, char symbol) throws ParseException {
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
            else if (current == symbol)
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
}
