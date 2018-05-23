package serializator;

import javafx.util.Pair;

import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Serializator {
    public static final String preamble = "<serialized>";
    public static final char screeningSymbol = '#';
    public static final String service = "()|" + screeningSymbol;

    public String serialize(Object object) {
        return preamble + object.getClass().getSimpleName() + '(' + allFieldsToString(object) + ')';
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
                return  new Pair<>(Types.String, screen((String)field.get(object)));
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

    public Object deserialize(byte[] raw) {
        StringReader reader = new StringReader(new String(raw, StandardCharsets.UTF_8));

    }

    private void readFields(StringReader reader, Object object) throws IOException{
        while (true) {
            int next = reader.read();
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

    private void readField(StringReader reader, Object object) {

    }

    private void setPrimitive(StringReader reader, Field field, Object object) {

    }

    /*private List<String> splitBrackets(String string) {
        if(!string.contains("(") && !string.contains(")"))
            return null;

        List<String> result = new ArrayList<>();
        int depth = 0;
        int start = 0;
        boolean screen = false;
        for(int i = 0; i < string.length(); i++) {
            char current = string.charAt(i);
            if(screen)
                screen = false;
            if(current == screeningSymbol)
                screen = true;
            else if(current == '(')
                depth++;
            else if(current == ')') {
                depth--;
                if(depth == 0) {
                    result.add(string.substring(start + 1, i));
                    start = i + 1;
                }
            }
            else if (depth == 0)
                throw new IllegalArgumentException();
        }
        if(depth != 0)
            throw new IllegalArgumentException();
        return result;
    }*/

    public String readUntil(StringReader string, char symbol) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean screen = false;
        while (true) {
            int next = string.read();
            if(next == -1)
                throw new IllegalArgumentException("Unexpected end of stream");
            char current = (char)next;

            if(screen)
                if(service.indexOf(current) != -1) {
                    sb.append(current);
                    screen = false;
                }
                else
                    throw new IllegalArgumentException("Invalid screening");
            else
                if(current == symbol)
                    return sb.toString();
                else if(current == screeningSymbol)
                    screen = true;
                else if(service.indexOf(current) == -1)
                    sb.append(current);
                else
                    throw new IllegalArgumentException("Unexpected service symbol at");
        }
    }
}
