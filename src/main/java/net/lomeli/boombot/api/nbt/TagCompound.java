package net.lomeli.boombot.api.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class TagCompound extends TagBase<Map<String, TagBase>> {
    private Map<String, TagBase> tagList;

    public TagCompound() {
        tagList = Maps.newHashMap();
    }

    public void setTag(String key, TagBase tag) {
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
        return hasTag(key) && getTag(key).getTagType() == type;
    }

    public void setByte(String key, byte value) {
        setTag(key, new TagByte(value));
    }

    public void setByteArray(String key, byte[] value) {
        setTag(key, new TagByteArray(value));
    }

    public void setDouble(String key, double value) {
        setTag(key, new TagDouble(value));
    }

    public void setFloat(String key, float value) {
        setTag(key, new TagFloat(value));
    }

    public void setInt(String key, int value) {
        setTag(key, new TagInt(value));
    }

    public void setLong(String key, long value) {
        setTag(key, new TagLong(value));
    }

    public void setShort(String key, short value) {
        setTag(key, new TagShort(value));
    }

    public void setString(String key, String value) {
        setTag(key, new TagString(value));
    }

    public void setIntArray(String key, int[] value) {
        setTag(key, new TagIntArray(value));
    }

    public TagBase getTag(String key) {
        return tagList.containsKey(key) ? tagList.get(key) : null;
    }

    public byte getByte(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagByte))
            return 0;
        return ((TagByte) tag).getValue();
    }

    public byte[] getByteArray(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagByteArray))
            return null;
        return ((TagByteArray) tag).getValue();
    }

    public TagCompound getTagCompound(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagCompound))
            return null;
        return (TagCompound) tag;
    }

    public double getDouble(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagDouble))
            return 0;
        return ((TagDouble) tag).getValue();
    }

    public float getFloat(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagFloat))
            return 0f;
        return ((TagFloat) tag).getValue();
    }

    public int[] getIntArray(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagIntArray))
            return null;
        return ((TagIntArray) tag).getValue();
    }

    public long getLong(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagLong))
            return 0;
        return ((TagLong) tag).getValue();
    }

    public short getShort(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagShort))
            return 0;
        return ((TagShort) tag).getValue();
    }

    public int getInt(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagInt))
            return 0;
        return ((TagInt) tag).getValue();
    }

    public String getString(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagString))
            return null;
        return ((TagString) tag).getValue();
    }

    public List<TagBase> getList(String key) {
        TagBase tag = getTag(key);
        if (tag == null || !(tag instanceof TagList))
            return Collections.EMPTY_LIST;
        return ((TagList) tag).getValue();
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        Iterator<String> iterator = this.tagList.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            TagBase tag = tagList.get(key);
            stream.writeByte(tag.getTagType().getId());
            if (tag.getTagType() != TagType.TAG_END) {
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
            TagBase tag = NBTUtil.getTagFromID(b0);
            if (tag != null) {
                tag.read(stream);
                tagList.put(key, tag);
            }
            b0 = stream.readByte();
        }
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_COMPOUND;
    }

    @Override
    public Map<String, TagBase> getValue() {
        return Collections.unmodifiableMap(this.tagList);
    }

    public TagCompound copy() {
        TagCompound copy = new TagCompound();
        copy.tagList.putAll(getValue());
        return copy;
    }
}
