package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagString extends TagBase<String> {
    private String value;

    public TagString() {
    }

    public TagString(String value) {
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
    public TagType getTagType() {
        return TagType.TAG_STRING;
    }
}
