package com.pain.tom.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufTest {

    // 底层占用字节数
    // capacity()

    // 底层最大能够占用的字节数
    // maxCapacity()

    // 当前可读的字节数
    // readableBytes()

    // 是否可读
    // isReadable()

    // 当前可写字节数
    // writableBytes()

    // capacity 是否等于 writerIndex，是否可写
    // isWritable()

    // 可写最大字节数
    // maxWritableBytes()

    // 返回当前的读指针
    // readerIndex()

    // 设置读指针
    // readerIndex(int)

    // 返回当前的写指针
    // writeIndex()

    // 设置写指针
    // writeIndex(int)

    // 把当前的读指针保存起来
    // markReaderIndex()

    // 把当前的读指针恢复到之前保存的值
    // resetReaderIndex()

    // markWriterIndex()
    // resetWriterIndex()

    // 读写 API
    // writeBytes(byte[] src)
    // readBytes(byte[] dst)

    // 内存管理
    // retain()
    // release()

    // 从原始 ByteBuf 中截取一段，这段数据是从 readerIndex 到 writeIndex
    // 返回的新的 ByteBuf 的最大容量 maxCapacity 为原始 ByteBuf 的 readableBytes()
    // slice()

    // 把整个 ByteBuf 都截取出来，包括所有的数据，指针信息
    // duplicate()

    // slice() 方法与 duplicate() 方法的相同点是：底层内存以及引用计数与原始的 ByteBuf 共享
    // slice() 方法与 duplicate() 不同点就是：slice() 只截取从 readerIndex 到 writerIndex 之间的数据，而 duplicate() 截取整个 ByteBuf

    // 会直接从原始的 ByteBuf 中拷贝所有的信息，包括读写指针以及底层对应的数据
    // copy()

    // retainedSlice() <=> slice().retain()
    // retainedDuplicate() <=> duplicate().retain()

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10, 100);

        buf.writeBytes(new byte[]{1, 2, 3, 4});
        printBuf("writeBytes(new byte[]{1, 2, 3, 4})", buf);

        buf.writeInt(100);
        printBuf("buf.writeInt(100)", buf);

        buf.writeBytes(new byte[]{5, 6});
        printBuf("buf.writeBytes(new byte[]{5, 6})", buf);

        // 发现不可写开始扩容
        buf.writeBytes(new byte[]{7, 8});
        printBuf("buf.writeBytes(new byte[]{7, 8})", buf);


        // get/set 方法不改变读指针
        System.out.println("getByte(3) return: " + buf.getByte(3));
        System.out.println("getShort(3) return: " + buf.getShort(3));
        System.out.println("getInt(3) return: " + buf.getInt(3));

        printBuf("buf.getByte()", buf);

        buf.setByte(buf.readableBytes() + 1, 0);
        printBuf("buf.setByte()", buf);

        // read 方法改变读指针
        byte[] dst = new byte[buf.readableBytes()];
        buf.readBytes(dst);
        printBuf("buf.readBytes()", buf);
    }

    private static void printBuf(String action, ByteBuf buf) {
        System.out.println("==========  " + action + "  ==========");
        System.out.println(String.format("capacity: %d, maxCapacity: %d, readerIndex: %d, readableBytes: %d",
                buf.capacity(), buf.maxCapacity(), buf.readerIndex(), buf.readableBytes()));
        System.out.println("isReadable: " + buf.isReadable());
        System.out.println(String.format("writerIndex: %d, writableBytes: %d", buf.writerIndex(), buf.writableBytes()));
        System.out.println("isWritable: " + buf.isWritable() + ", maxWritableBytes: " + buf.maxWritableBytes());
    }
}
