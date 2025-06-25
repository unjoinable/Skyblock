package net.unjoinable.skyblock.utility;

import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;

import java.util.*;
import java.util.function.Function;

public final class TranscoderYamlImpl implements Transcoder<Object> {
    public static final Transcoder<Object> INSTANCE = new TranscoderYamlImpl();
    private static final Set<String> TRUE_VALUES = Set.of("true", "yes", "on");
    private static final Set<String> FALSE_VALUES = Set.of("false", "no", "off");

    private TranscoderYamlImpl() {}

    @Override
    public Object createNull() {
        return new Object();
    }

    @Override
    public Result<Boolean> getBoolean(Object value) {
        return switch (value) {
            case Boolean b -> new Result.Ok<>(b);
            case String s -> {
                String lower = s.toLowerCase();
                if (TRUE_VALUES.contains(lower)) {
                    yield new Result.Ok<>(true);
                }
                if (FALSE_VALUES.contains(lower)) {
                    yield new Result.Ok<>(false);
                }
                yield new Result.Error<>("Not a boolean: " + value);
            }
            default -> new Result.Error<>("Not a boolean: " + value);
        };
    }

    @Override
    public Object createBoolean(boolean value) {
        return value;
    }

    @Override
    public Result<Byte> getByte(Object value) {
        return getNumber(value, Number::byteValue, Byte::parseByte, "byte");
    }

    @Override
    public Object createByte(byte value) {
        return value;
    }

    @Override
    public Result<Short> getShort(Object value) {
        return getNumber(value, Number::shortValue, Short::parseShort, "short");
    }

    @Override
    public Object createShort(short value) {
        return value;
    }

    @Override
    public Result<Integer> getInt(Object value) {
        return getNumber(value, Number::intValue, Integer::parseInt, "int");
    }

    @Override
    public Object createInt(int value) {
        return value;
    }

    @Override
    public Result<Long> getLong(Object value) {
        return getNumber(value, Number::longValue, Long::parseLong, "long");
    }

    @Override
    public Object createLong(long value) {
        return value;
    }

    @Override
    public Result<Float> getFloat(Object value) {
        return getNumber(value, Number::floatValue, Float::parseFloat, "float");
    }

    @Override
    public Object createFloat(float value) {
        return value;
    }

    @Override
    public Result<Double> getDouble(Object value) {
        return getNumber(value, Number::doubleValue, Double::parseDouble, "double");
    }

    @Override
    public Object createDouble(double value) {
        return value;
    }

    private <T> Result<T> getNumber(Object value, Function<Number, T> extractor,
                                    Function<String, T> parser, String type) {
        return switch (value) {
            case Number n -> new Result.Ok<>(extractor.apply(n));
            case String s -> {
                try {
                    yield new Result.Ok<>(parser.apply(s));
                } catch (NumberFormatException _) {
                    yield new Result.Error<>("Not a valid " + type + ": " + value);
                }
            }
            default -> new Result.Error<>("Not a " + type + ": " + value);
        };
    }

    @Override
    public Result<String> getString(Object value) {
        if (value instanceof String s) {
            return new Result.Ok<>(s);
        }
        return new Result.Ok<>(value.toString());
    }

    @Override
    public Object createString(String value) {
        return value;
    }

    @Override
    public Result<List<Object>> getList(Object value) {
        if (!(value instanceof List<?> list)) {
            return new Result.Error<>("Not a list: " + value);
        }
        return new Result.Ok<>(list.isEmpty() ? List.of() : List.copyOf(list));
    }

    @Override
    public Object emptyList() {
        return new ArrayList<>();
    }

    @Override
    public ListBuilder<Object> createList(int expectedSize) {
        List<Object> list = new ArrayList<>(expectedSize);
        return new ListBuilder<>() {
            @Override
            public ListBuilder<Object> add(Object value) {
                list.add(value);
                return this;
            }

            @Override
            public Object build() {
                return list;
            }
        };
    }

    @Override
    public Result<MapLike<Object>> getMap(Object value) {
        if (!(value instanceof Map<?, ?> map)) {
            return new Result.Error<>("Not a map: " + value);
        }

        return new Result.Ok<>(new MapLike<>() {
            @Override
            public Collection<String> keys() {
                return map.keySet().stream()
                        .map(k -> k != null ? k.toString() : "null")
                        .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
            }

            @Override
            public boolean hasValue(String key) {
                return map.containsKey(key) ||
                        map.keySet().stream().anyMatch(k -> key.equals(toString(k)));
            }

            @Override
            public Result<Object> getValue(String key) {
                if (map.containsKey(key)) {
                    return new Result.Ok<>(map.get(key));
                }

                return map.entrySet().stream()
                        .filter(entry -> key.equals(toString(entry.getKey())))
                        .findFirst()
                        .<Result<Object>>map(entry -> new Result.Ok<>(entry.getValue()))
                        .orElse(new Result.Error<>("No such key: " + key));
            }

            @Override
            public int size() {
                return map.size();
            }

            private String toString(Object obj) {
                return obj != null ? obj.toString() : "null";
            }
        });
    }

    @Override
    public Object emptyMap() {
        return new LinkedHashMap<>();
    }

    @Override
    public MapBuilder<Object> createMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        return new MapBuilder<>() {
            @Override
            public MapBuilder<Object> put(Object key, Object value) {
                return put(key.toString(), value);
            }

            @Override
            public MapBuilder<Object> put(String key, Object value) {
                if (value != null) map.put(key, value);
                return this;
            }

            @Override
            public Object build() {
                return map;
            }
        };
    }

    @Override
    public <O> Result<O> convertTo(Transcoder<O> coder, Object value) {
        return switch (value) {
            case Map<?, ?> map -> convertMap(coder, map);
            case List<?> list -> convertList(coder, list);
            case Boolean b -> new Result.Ok<>(coder.createBoolean(b));
            case Integer i -> new Result.Ok<>(coder.createInt(i));
            case Long l -> new Result.Ok<>(coder.createLong(l));
            case Float f -> new Result.Ok<>(coder.createFloat(f));
            case Double d -> new Result.Ok<>(coder.createDouble(d));
            case Byte b -> new Result.Ok<>(coder.createByte(b));
            case Short s -> new Result.Ok<>(coder.createShort(s));
            case Number n -> new Result.Ok<>(coder.createDouble(n.doubleValue()));
            case String s -> new Result.Ok<>(coder.createString(s));
            default -> new Result.Error<>("Unknown YAML type: " + value.getClass().getSimpleName());
        };
    }

    private <O> Result<O> convertMap(Transcoder<O> coder, Map<?, ?> map) {
        var builder = coder.createMap();
        for (var entry : map.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey().toString() : "null";
            switch (convertTo(coder, entry.getValue())) {
                case Result.Ok(O data) -> builder.put(key, data);
                case Result.Error(String message) -> {
                    return new Result.Error<>(key + ": " + message);
                }
            }
        }
        return new Result.Ok<>(builder.build());
    }

    private <O> Result<O> convertList(Transcoder<O> coder, List<?> list) {
        if (list.isEmpty()) return new Result.Ok<>(coder.emptyList());

        var builder = coder.createList(list.size());
        for (int i = 0; i < list.size(); i++) {
            switch (convertTo(coder, list.get(i))) {
                case Result.Ok(O data) -> builder.add(data);
                case Result.Error(String message) -> {
                    return new Result.Error<>(i + ": " + message);
                }
            }
        }
        return new Result.Ok<>(builder.build());
    }
}