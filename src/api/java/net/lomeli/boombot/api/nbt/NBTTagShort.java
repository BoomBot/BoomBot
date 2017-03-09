package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTTagBase<Short> {
    private short value;

    public NBTTagShort() {
    }

    public NBTTagShort(short value) {
        this.value = value;
    }

    @Override
    public Short getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeShort(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        value = stream.readShort();
    }

    @Override
    public byte getID() {
        return TagType.TAG_SHORT.getId();
    }
}
