package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagIntArray extends NBTTagBase<int[]> {
    private int[] value;

    public NBTTagIntArray() {
    }

    public NBTTagIntArray(int[] value) {
        this.value = value;
    }

    @Override
    public int[] getValue() {
        return value;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        stream.writeInt(value.length);
        for (int i = 0; i < value.length; i++)
            stream.writeInt(value[i]);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        int size = stream.readInt();
        value = new int[size];
        for (int i = 0; i < size; i++)
            value[i] = stream.readInt();
    }

    @Override
    public byte getID() {
        return TagType.TAG_INT_ARRAY.getId();
    }
}
