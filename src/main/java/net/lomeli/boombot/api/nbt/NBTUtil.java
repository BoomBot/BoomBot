package net.lomeli.boombot.api.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTUtil {
    public static TagBase getTagFromType(TagBase.TagType type) {
        switch (type) {
            case TAG_END:
                return new TagEnd();
            case TAG_BYTE:
                return new TagByte();
            case TAG_SHORT:
                return new TagShort();
            case TAG_INT:
                return new TagInt();
            case TAG_LONG:
                return new TagLong();
            case TAG_FLOAT:
                return new TagFloat();
            case TAG_DOUBLE:
                return new TagDouble();
            case TAG_BYTE_ARRAY:
                return new TagByteArray();
            case TAG_STRING:
                return new TagString();
            case TAG_LIST:
                return new TagList();
            case TAG_COMPOUND:
                return new TagCompound();
            case TAG_INT_ARRAY:
                return new TagIntArray();
            default:
                return null;
        }
    }

    /**
     * Get blank instance of tag based on id
     *
     * @param id
     * @return
     */
    public static TagBase getTagFromID(byte id) {
        return getTagFromType(TagBase.TagType.getTagFromByte(id));
    }

    /**
     * Write nbt file using gzip compression
     *
     * @param baseTag
     * @param stream
     * @throws IOException
     */
    public static void writeCompressed(TagCompound baseTag, OutputStream stream) throws IOException {
        DataOutputStream outStream = new DataOutputStream(new GZIPOutputStream(stream));
        write(baseTag, outStream);
    }

    /**
     * Write nbt file without compression
     *
     * @param baseTag
     * @param file
     * @throws IOException
     */
    public static void writeUncompressed(TagCompound baseTag, File file) throws IOException {
        writeUncompressed(baseTag, new FileOutputStream(file));
    }

    public static void writeUncompressed(TagCompound baseTag, FileOutputStream out) throws IOException {
        DataOutputStream outStream = new DataOutputStream(out);
        write(baseTag, outStream);
    }

    /**
     * Write nbt file
     *
     * @param baseTag
     * @param outStream
     * @throws IOException
     */
    public static void write(TagCompound baseTag, DataOutputStream outStream) throws IOException {
        try {
            outStream.writeByte(10);
            outStream.writeUTF("");
            baseTag.write(outStream);
        } finally {
            outStream.flush();
            outStream.close();
        }
    }

    /**
     * Read nbt file without compression
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static TagCompound readUncompressed(File file) throws IOException {
        return readUncompressed(new FileInputStream(file));
    }

    public static TagCompound readUncompressed(FileInputStream in) throws IOException {
        DataInputStream inStream = new DataInputStream(in);
        return read(inStream);
    }

    /**
     * Read nbt file with gzip compression
     *
     * @param stream
     * @return
     * @throws IOException
     */
    public static TagCompound readCompressed(InputStream stream) throws IOException {
        DataInputStream inStream = new DataInputStream(new GZIPInputStream(stream));
        return read(inStream);
    }

    /**
     * Read nbt file
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static TagCompound read(DataInputStream inStream) throws IOException {
        TagCompound tag = new TagCompound();
        try {
            if (inStream.readByte() == TagBase.TagType.TAG_COMPOUND.getId()) {
                inStream.readUTF();
                tag.read(inStream);
            }
        } finally {
            inStream.close();
        }
        return tag;
    }
}
