package net.lomeli.boombot.api.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class NBTTagCompound extends NBTTagBase<Map<String, NBTTagBase>> {
    private Map<String, NBTTagBase> tagList;

    public NBTTagCompound() {
        tagList = Maps.newHashMap();
    }

    public void setTag(String key, NBTTagBase tag) {
        if (tag != null)
            tagList.put(key, tag);
    }

    public int getTagCount() {
        return tagList.size();
    }

    public Collection<String> getKeys() {
        return Collections.unmodifiableList(Lists.newArrayList(tagList.keySet()));
    }

    public boolean hasTag(String key) {
        return tagList.containsKey(key) && getTag(key) != null;
    }

    public boolean hasTag(String key, TagType type) {
        return hasTag(key) && getTag(key).getID() == type.getId();
    }

    public void setByte(String key, byte value) {
        setTag(key, new NBTTagByte(value));
    }

    public void setByteArray(String key, byte[] value) {
        setTag(key, new NBTTagByteArray(value));
    }

    public void setDouble(String key, double value) {
        setTag(key, new NBTTagDouble(value));
    }

    public void setFloat(String key, float value) {
        setTag(key, new NBTTagFloat(value));
    }

    public void setInt(String key, int value) {
        setTag(key, new NBTTagInt(value));
    }

    public void setLong(String key, long value) {
        setTag(key, new NBTTagLong(value));
    }

    public void setShort(String key, short value) {
        setTag(key, new NBTTagShort(value));
    }

    public void setString(String key, String value) {
        setTag(key, new NBTTagString(value));
    }

    public void setIntArray(String key, int[] value) {
        setTag(key, new NBTTagIntArray(value));
    }

    public NBTTagBase getTag(String key) {
        return tagList.containsKey(key) ? tagList.get(key) : null;
    }

    public byte getByte(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagByte))
            return 0;
        return ((NBTTagByte) tag).getValue();
    }

    public byte[] getByteArray(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagByteArray))
            return null;
        return ((NBTTagByteArray) tag).getValue();
    }

    public NBTTagCompound getTagCompound(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagCompound))
            return null;
        return (NBTTagCompound) tag;
    }

    public double getDouble(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagDouble))
            return 0;
        return ((NBTTagDouble) tag).getValue();
    }

    public float getFloat(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagFloat))
            return 0f;
        return ((NBTTagFloat) tag).getValue();
    }

    public int[] getIntArray(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagIntArray))
            return null;
        return ((NBTTagIntArray) tag).getValue();
    }

    public long getLong(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagLong))
            return 0;
        return ((NBTTagLong) tag).getValue();
    }

    public short getShort(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagShort))
            return 0;
        return ((NBTTagShort) tag).getValue();
    }

    public int getInt(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagInt))
            return 0;
        return ((NBTTagInt) tag).getValue();
    }

    public String getString(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagString))
            return null;
        return ((NBTTagString) tag).getValue();
    }

    public List<NBTTagBase> getList(String key) {
        NBTTagBase tag = getTag(key);
        if (tag == null || !(tag instanceof NBTTagList))
            return Collections.EMPTY_LIST;
        return ((NBTTagList) tag).getValue();
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        Iterator<String> iterator = this.tagList.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            NBTTagBase tag = tagList.get(key);
            stream.writeByte(tag.getID());
            if (tag.getID() != 0) {
                stream.writeUTF(key);
                tag.write(stream);
            }
        }
        stream.writeByte(0);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        byte b0 = stream.readByte();
        while (b0 != TagType.TAG_END.getId()) {
            String key = stream.readUTF();
            NBTTagBase tag = NBTUtil.getTagFromID(b0);
            if (tag != null) {
                tag.read(stream);
                tagList.put(key, tag);
            }
            b0 = stream.readByte();
        }
    }

    @Override
    public byte getID() {
        return TagType.TAG_COMPOUND.getId();
    }

    @Override
    public Map<String, NBTTagBase> getValue() {
        return Collections.unmodifiableMap(this.tagList);
    }

    public NBTTagCompound copy() {
        NBTTagCompound copy = new NBTTagCompound();
        copy.tagList = Collections.unmodifiableMap(this.tagList);
        return copy;
    }
}
