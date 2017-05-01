package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagShort extends TagBase<Short> {
    private short value;

    public TagShort() {
    }

    public TagShort(short value) {
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
    public TagType getTagType() {
        return TagType.TAG_SHORT;
    }
}
