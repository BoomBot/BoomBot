package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagLong extends TagBase<Long> {
    private long value;

    public TagLong() {
    }

    public TagLong(long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeLong(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        value = stream.readLong();
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_LONG;
    }
}
