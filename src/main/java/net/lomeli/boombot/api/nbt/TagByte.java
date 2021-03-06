package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagByte extends TagBase<Byte> {
    private byte value;

    public TagByte() {
    }

    public TagByte(byte value) {
        this.value = value;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeByte(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        value = stream.readByte();
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_BYTE;
    }
}
