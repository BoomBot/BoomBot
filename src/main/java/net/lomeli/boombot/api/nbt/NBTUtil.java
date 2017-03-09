package net.lomeli.boombot.api.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTUtil {
    /**
     * Get blank instance of tag based on id
     *
     * @param id
     * @return
     */
    public static NBTTagBase getTagFromID(byte id) {
        switch (id) {
            case 0:
                return new NBTTagEnd();
            case 1:
                return new NBTTagByte();
            case 2:
                return new NBTTagShort();
            case 3:
                return new NBTTagInt();
            case 4:
                return new NBTTagLong();
            case 5:
                return new NBTTagFloat();
            case 6:
                return new NBTTagDouble();
            case 7:
                return new NBTTagByteArray();
            case 8:
                return new NBTTagString();
            case 9:
                return new NBTTagList();
            case 10:
                return new NBTTagCompound();
            case 11:
                return new NBTTagIntArray();
        }
        return null;
    }

    /**
     * Write nbt file using gzip compression
     *
     * @param baseTag
     * @param stream
     * @throws IOException
     */
    public static void writeCompressed(NBTTagCompound baseTag, OutputStream stream) throws IOException {
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
    public static void writeUncompressed(NBTTagCompound baseTag, File file) throws IOException {
        DataOutputStream outStream = new DataOutputStream(new FileOutputStream(file));
        write(baseTag, outStream);
    }

    /**
     * Write nbt file
     *
     * @param baseTag
     * @param outStream
     * @throws IOException
     */
    public static void write(NBTTagCompound baseTag, DataOutputStream outStream) throws IOException {
        try {
            outStream.writeByte(10);
            outStream.writeUTF("");
            baseTag.write(outStream);
        } finally {
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
    public static NBTTagCompound readUncompressed(File file) throws IOException {
        DataInputStream inStream = new DataInputStream(new FileInputStream(file));
        return read(inStream);
    }

    /**
     * Read nbt file with gzip compression
     *
     * @param stream
     * @return
     * @throws IOException
     */
    public static NBTTagCompound readCompressed(InputStream stream) throws IOException {
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
    public static NBTTagCompound read(DataInputStream inStream) throws IOException {
        NBTTagCompound tag = new NBTTagCompound();
        try {
            if (inStream.readByte() == NBTTagBase.TagType.TAG_COMPOUND.getId()) {
                inStream.readUTF();
                tag.read(inStream);
            }
        } finally {
            inStream.close();
        }
        return tag;
    }
}
