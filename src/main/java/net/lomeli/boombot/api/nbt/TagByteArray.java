package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagByteArray extends TagBase<byte[]> {
    private byte[] value;

    public TagByteArray() {
    }

    public TagByteArray(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeInt(value.length);
        stream.write(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        int size = stream.readInt();
        value = new byte[size];
        stream.readFully(value);
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_BYTE_ARRAY;
    }
}
