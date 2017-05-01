package net.lomeli.boombot.api.nbt;

import com.google.common.collect.Lists;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class TagList extends TagBase<List<TagBase>> {
    private List<TagBase> list;
    private TagType type;

    public TagList(TagType type) {
        this.type = type;
        list = Lists.newArrayList();
    }

    public TagList() {
        this(TagType.TAG_END);
    }

    public boolean add(TagBase tag) {
        if (tag != null) {
            if (type == TagType.TAG_END)
                type = tag.getTagType();
            if (tag.getTagType() == type)
                return list.add(tag);
        }
        return false;
    }

    public boolean remove(TagBase tag) {
        return list.remove(tag);
    }

    public TagType getType() {
        return type;
    }

    public Stream<TagBase> stream() {
        return list.stream();
    }

    public int getTagCount() {
        return list.size();
    }

    @Override
    public List<TagBase> getValue() {
        return list;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeByte(type.getId());
        stream.writeInt(list.size());
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++)
                list.get(i).write(stream);
        }
    }

    @Override
    public void read(DataInput stream) throws IOException {
        byte typeID = stream.readByte();
        type = TagType.getTagFromByte(typeID);
        int size = stream.readInt();
        for (int i = 0; i < size; i++) {
            TagBase tag = NBTUtil.getTagFromID(typeID);
            if (tag != null) {
                tag.read(stream);
                add(tag);
            }
        }
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_LIST;
    }
}