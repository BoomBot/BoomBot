package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagDouble extends TagBase<Double> {
    private double value;

    public TagDouble() {
    }

    public TagDouble(double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeDouble(value);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        value = stream.readDouble();
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG_DOUBLE;
    }
}
