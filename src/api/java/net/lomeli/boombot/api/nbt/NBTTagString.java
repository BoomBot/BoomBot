package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTTagBase<String> {
    private String value;

    public NBTTagString() {
    }

    public NBTTagString(String value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeUTF(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        this.value = stream.readUTF();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public byte getID() {
        return TagType.TAG_STRING.getId();
    }
}
