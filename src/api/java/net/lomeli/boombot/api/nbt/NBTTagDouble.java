package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTTagBase<Double> {
    private double value;

    public NBTTagDouble() {
    }

    public NBTTagDouble(double value) {
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
    public byte getID() {
        return TagType.TAG_DOUBLE.getId();
    }
}
