package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagInt extends TagBase<Integer> {
    private int value;

    public TagInt() {
    }

    public TagInt(int value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeInt(this.value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        this.value = stream.readInt();
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_INT;
    }
}
