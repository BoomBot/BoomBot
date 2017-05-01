package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagEnd extends TagBase<Object> {

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
    public TagType getTagType() {
        return TagType.TAG_END;
    }
}
