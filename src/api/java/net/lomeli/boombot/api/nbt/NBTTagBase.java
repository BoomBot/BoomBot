package net.lomeli.boombot.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTTagBase<T> {
    /**
     * Write tag value to stream
     *
     * @param stream
     * @throws IOException
     */
    public abstract void write(DataOutput stream) throws IOException;

    /**
     * Read tag value from stream
     *
     * @param stream
     * @throws IOException
     */
    public abstract void read(DataInput stream) throws IOException;

    public abstract byte getID();

    public abstract T getValue();

    @Override
    public int hashCode() {
        return getID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof NBTTagBase)
            return this.getID() == ((NBTTagBase) obj).getID();
        return false;
    }

    public static enum TagType {
        TAG_END(0), TAG_BYTE(1), TAG_SHORT(2), TAG_INT(3), TAG_LONG(4), TAG_FLOAT(5), TAG_DOUBLE(6),
        TAG_BYTE_ARRAY(7), TAG_STRING(8), TAG_LIST(9), TAG_COMPOUND(10), TAG_INT_ARRAY(11);
        private static final TagType[] VALID_TYPES = {TAG_END, TAG_BYTE, TAG_SHORT, TAG_INT, TAG_LONG,
                TAG_FLOAT, TAG_DOUBLE, TAG_BYTE_ARRAY, TAG_STRING, TAG_LIST, TAG_COMPOUND, TAG_INT_ARRAY};

        private final byte id;

        private TagType(byte id) {
            this.id = id;
        }

        private TagType(int id) {
            this((byte) id);
        }

        public byte getId() {
            return id;
        }

        public static TagType getTagFromByte(byte b) {
            return (b >= 0 && b < VALID_TYPES.length) ? VALID_TYPES[b] : TAG_END;
        }
    }
}
