/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.fabric;

import com.sk89q.jnbt.*;
import net.minecraft.nbt.*;

import java.util.*;

/**
 * Converts between JNBT and Minecraft NBT classes.
 */
public final class NbtConverter {

    private NbtConverter() {}

    public static NbtElement toNative(Tag tag) {
        if (tag instanceof IntArrayTag) {
            return toNative((IntArrayTag) tag);

        } else if (tag instanceof ListTag) {
            return toNative((ListTag) tag);

        } else if (tag instanceof LongTag) {
            return toNative((LongTag) tag);

        } else if (tag instanceof StringTag) {
            return toNative((StringTag) tag);

        } else if (tag instanceof IntTag) {
            return toNative((IntTag) tag);

        } else if (tag instanceof ByteTag) {
            return toNative((ByteTag) tag);

        } else if (tag instanceof ByteArrayTag) {
            return toNative((ByteArrayTag) tag);

        } else if (tag instanceof CompoundTag) {
            return toNative((CompoundTag) tag);

        } else if (tag instanceof FloatTag) {
            return toNative((FloatTag) tag);

        } else if (tag instanceof ShortTag) {
            return toNative((ShortTag) tag);

        } else if (tag instanceof DoubleTag) {
            return toNative((DoubleTag) tag);
        } else {
            throw new IllegalArgumentException("Can't convert tag of type " + tag.getClass().getCanonicalName());
        }
    }

    public static NbtIntArray toNative(IntArrayTag tag) {
        int[] value = tag.getValue();
        return new NbtIntArray(Arrays.copyOf(value, value.length));
    }

    public static NbtList toNative(ListTag tag) {
        NbtList list = new NbtList();

        for (Tag child : tag.getValue()) {
            if (child instanceof EndTag) {
                continue;
            }

            list.add(toNative(child));
        }

        return list;
    }

    public static NbtLong toNative(LongTag tag) {
        return new NbtLong(tag.getValue());
    }

    public static NbtString toNative(StringTag tag) {
        return new NbtString(tag.getValue());
    }

    public static NbtInt toNative(IntTag tag) {
        return new NbtInt(tag.getValue());
    }

    public static NbtByte toNative(ByteTag tag) {
        return new NbtByte(tag.getValue());
    }

    public static NbtByteArray toNative(ByteArrayTag tag) {
        byte[] value = tag.getValue();
        return new NbtByteArray(Arrays.copyOf(value, value.length));
    }

    public static NbtCompound toNative(CompoundTag tag) {
        NbtCompound compound = new NbtCompound();

        for (Map.Entry<String, Tag> child : tag.getValue().entrySet()) {
            compound.put(child.getKey(), toNative(child.getValue()));
        }

        return compound;
    }

    public static NbtFloat toNative(FloatTag tag) {
        return new NbtFloat(tag.getValue());
    }

    public static NbtShort toNative(ShortTag tag) {
        return new NbtShort(tag.getValue());
    }

    public static NbtDouble toNative(DoubleTag tag) {
        return new NbtDouble(tag.getValue());
    }

    public static Tag fromNative(NbtElement other) {
        if (other instanceof NbtIntArray) {
            return fromNative((NbtIntArray) other);

        } else if (other instanceof NbtList) {
            return fromNative((NbtList) other);

        } else if (other instanceof NbtEnd) {
            return fromNative((NbtEnd) other);

        } else if (other instanceof NbtLong) {
            return fromNative((NbtLong) other);

        } else if (other instanceof NbtString) {
            return fromNative((NbtString) other);

        } else if (other instanceof NbtInt) {
            return fromNative((NbtInt) other);

        } else if (other instanceof NbtByte) {
            return fromNative((NbtByte) other);

        } else if (other instanceof NbtByteArray) {
            return fromNative((NbtByteArray) other);

        } else if (other instanceof NbtCompound) {
            return fromNative((NbtCompound) other);

        } else if (other instanceof NbtFloat) {
            return fromNative((NbtFloat) other);

        } else if (other instanceof NbtShort) {
            return fromNative((NbtShort) other);

        } else if (other instanceof NbtDouble) {
            return fromNative((NbtDouble) other);
        } else {
            throw new IllegalArgumentException("Can't convert other of type " + other.getClass().getCanonicalName());
        }
    }

    public static IntArrayTag fromNative(NbtIntArray other) {
        int[] value = other.getIntArray();
        return new IntArrayTag(Arrays.copyOf(value, value.length));
    }

    public static ListTag fromNative(NbtList other) {
        other = (NbtList) other.copy();
        List<Tag> list = new ArrayList<>();
        Class<? extends Tag> listClass = StringTag.class;
        int tags = other.size();

        for (int i = 0; i < tags; i++) {
            Tag child = fromNative(other.remove(0));
            list.add(child);
            listClass = child.getClass();
        }

        return new ListTag(listClass, list);
    }

    public static EndTag fromNative(NbtEnd other) {
        return new EndTag();
    }

    public static LongTag fromNative(NbtLong other) {
        return new LongTag(other.getLong());
    }

    public static StringTag fromNative(NbtString other) {
        return new StringTag(other.asString());
    }

    public static IntTag fromNative(NbtInt other) {
        return new IntTag(other.getInt());
    }

    public static ByteTag fromNative(NbtByte other) {
        return new ByteTag(other.getByte());
    }

    public static ByteArrayTag fromNative(NbtByteArray other) {
        byte[] value = other.getByteArray();
        return new ByteArrayTag(Arrays.copyOf(value, value.length));
    }

    public static CompoundTag fromNative(NbtCompound other) {
        Set<String> tags = other.getKeys();
        Map<String, Tag> map = new HashMap<>();

        for (String tagName : tags) {
            map.put(tagName, fromNative(other.get(tagName)));
        }

        return new CompoundTag(map);
    }

    public static FloatTag fromNative(NbtFloat other) {
        return new FloatTag(other.getFloat());
    }

    public static ShortTag fromNative(NbtShort other) {
        return new ShortTag(other.getShort());
    }

    public static DoubleTag fromNative(NbtDouble other) {
        return new DoubleTag(other.getDouble());
    }

}
