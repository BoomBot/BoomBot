package net.lomeli.boombot.api.nbt;

import com.google.common.collect.Lists;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class NBTTagList extends NBTTagBase<List<NBTTagBase>> {
    private List<NBTTagBase> list;
    private TagType type;

    public NBTTagList(TagType type) {
        this.type = type;
        list = Lists.newArrayList();
    }

    public NBTTagList() {
        this(TagType.TAG_END);
    }

    public boolean add(NBTTagBase tag) {
        if (tag != null) {
            if (type.getId() == 0)
                type = TagType.getTagFromByte(tag.getID());
            if (tag.getID() == type.getId())
                return list.add(tag);
        }
        return false;
    }

    public boolean remove(NBTTagBase tag) {
        return list.remove(tag);
    }

    public TagType getType() {
        return type;
    }

    public Stream<NBTTagBase> stream() {
        return list.stream();
    }

    public int getTagCount() {
        return list.size();
    }

    @Override
    public List<NBTTagBase> getValue() {
        return list;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        if (!list.isEmpty()) {
            stream.writeByte(type.getId());
            stream.writeInt(list.size());
            for (int i = 0; i < list.size(); i++)
                list.get(i).write(stream);
        }
    }

    @Override
    public void read(DataInput stream) throws IOException {
        type = TagType.getTagFromByte(stream.readByte());
        int size = stream.readInt();
        for (int i = 0; i < size; i++) {
            NBTTagBase tag = NBTUtil.getTagFromID(type.getId());
            if (tag != null) {
                tag.read(stream);
                add(tag);
            }
        }
    }

    @Override
    public byte getID() {
        return TagType.TAG_LIST.getId();
    }
}