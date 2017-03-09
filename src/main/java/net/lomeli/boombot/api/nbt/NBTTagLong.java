package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTTagBase<Long> {
    private long value;

    public NBTTagLong() {
    }

    public NBTTagLong(long value) {
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
    public byte getID() {
        return TagType.TAG_LONG.getId();
    }
}
