package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagFloat extends TagBase<Float> {
    private float value;

    public TagFloat() {
    }

    public TagFloat(float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeFloat(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        value = stream.readFloat();
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_FLOAT;
    }
}
