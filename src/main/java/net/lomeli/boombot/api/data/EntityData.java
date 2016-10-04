package net.lomeli.boombot.api.data;

import com.google.common.collect.Maps;

import java.util.Map;

public class EntityData {
    private Map<String, Object> entityData;

    public EntityData() {
        entityData = Maps.newHashMap();
    }

    public boolean hasKey(String key) {
        return entityData.containsKey(key);
    }

    private Object get(String key) {
        if (hasKey(key)) {
            Object value = entityData.get(key);
            return value;
        }
        return null;
    }

    private <T> T set(String key, T value) {
        T old = hasKey(key) ? (T) get(key) : value;
        entityData.put(key, value);
        return old;
    }

    public byte setByte(String key, byte value) {
        return set(key, value);
    }

    public byte getByte(String key) {
        Object value = get(key);
        return (value != null && value instanceof Byte) ? (byte) value : 0;
    }

    public short setShort(String key, short value) {
        return set(key, value);
    }

    public short getShort(String key) {
        Object value = get(key);
        return (value != null && value instanceof Short) ? (short) value : 0;
    }

    public int setInteger(String key, int value) {
        return set(key, value);
    }

    public int getInteger(String key) {
        Object value = get(key);
        return (value != null && value instanceof Integer) ? (int) value : 0;
    }

    public long setLong(String key, long value) {
        return set(key, value);
    }

    public long getLong(String key) {
        Object value = get(key);
        return (value != null && value instanceof Long) ? (long) value : 0;
    }

    public float setFloat(String key, float value) {
        return set(key, value);
    }

    public float getFloat(String key) {
        Object value = get(key);
        return (value != null && value instanceof Float) ? (float) value : 0f;
    }

    public double setDouble(String key, double value) {
        return set(key, value);
    }

    public double getDouble(String key) {
        Object value = get(key);
        return (value != null && value instanceof Double) ? (double) value : 0d;
    }

    public String setString(String key, String value) {
        return set(key, value);
    }

    public String getString(String key) {
        Object value = get(key);
        return (value != null && value instanceof String) ? (String) value : "";
    }

    public boolean setBoolean(String key, boolean value) {
        return set(key, value);
    }

    public boolean getBoolean(String key) {
        Object value = get(key);
        return (value != null && value instanceof Boolean) ? (boolean) value : false;
    }

    public int[] setIntArray(String key, int[] value) {
        return set(key, value);
    }

    public int[] getIntArray(String key) {
        Object value = get(key);
        return (value != null && value instanceof int[]) ? (int[]) value : new int[0];
    }

    public float[] setFloatArray(String key, float[] value) {
        return set(key, value);
    }

    public float[] getFloatArray(String key) {
        Object value = get(key);
        return (value != null && value instanceof float[]) ? (float[]) value : new float[0];
    }

    public String[] setStringArray(String key, String[] value) {
        return set(key, value);
    }

    public String[] getStringArray(String key) {
        Object value = get(key);
        return (value != null && value instanceof String[]) ? (String[]) value : new String[0];
    }

    public EntityData setData(String key, EntityData value) {
        return set(key, value);
    }

    public EntityData getData(String key) {
        Object value = get(key);
        return (value != null && value instanceof EntityData) ? (EntityData) value : new EntityData();
    }
}
