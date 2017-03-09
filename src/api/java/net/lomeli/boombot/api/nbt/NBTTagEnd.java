package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTTagBase<Object> {

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void write(DataOutput stream) throws IOException {
    }

    @Override
    public void read(DataInput stream) throws IOException {
    }

    @Override
    public byte getID() {
        return TagType.TAG_END.getId();
    }
}
