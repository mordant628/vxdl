package vxdl;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SleepyBuffer implements Buffer {
  private static final Logger LOGGER = LoggerFactory.getLogger(SleepyBuffer.class);

    private String data;
    public SleepyBuffer(String d){
        this.data = d;
    }

    public void writeToBuffer(Buffer buffer){
        throw new UnsupportedOperationException();
    }

    public int readFromBuffer(int pos, Buffer buffer){
        throw new UnsupportedOperationException();
    }

    public String toString(){
        try{
            long sleepMillis = 3000;
            LOGGER.warn("SLEEPING for "+sleepMillis+"ms");
            Thread.sleep(sleepMillis);
        }catch(InterruptedException ignored){
            //noop
        }
        return data;
    }

    public String toString(String var1){
        throw new UnsupportedOperationException();
    }

    public String toString(Charset var1){
        throw new UnsupportedOperationException();
    }

    public JsonObject toJsonObject(){
        throw new UnsupportedOperationException();
    }

    public JsonArray toJsonArray(){
        throw new UnsupportedOperationException();
    }

    public byte getByte(int var1){
        throw new UnsupportedOperationException();
    }

    public short getUnsignedByte(int var1){
        throw new UnsupportedOperationException();
    }

    public int getInt(int var1){
        throw new UnsupportedOperationException();
    }

    public int getIntLE(int var1){
        throw new UnsupportedOperationException();
    }

    public long getUnsignedInt(int var1){
        throw new UnsupportedOperationException();
    }

    public long getUnsignedIntLE(int var1){
        throw new UnsupportedOperationException();
    }

    public long getLong(int var1){
        throw new UnsupportedOperationException();
    }

    public long getLongLE(int var1){
        throw new UnsupportedOperationException();
    }

    public double getDouble(int var1){
        throw new UnsupportedOperationException();
    }

    public float getFloat(int var1){
        throw new UnsupportedOperationException();
    }

    public short getShort(int var1){
        throw new UnsupportedOperationException();
    }

    public short getShortLE(int var1){
        throw new UnsupportedOperationException();
    }

    public int getUnsignedShort(int var1){
        throw new UnsupportedOperationException();
    }

    public int getUnsignedShortLE(int var1){
        throw new UnsupportedOperationException();
    }

    public int getMedium(int var1){
        throw new UnsupportedOperationException();
    }

    public int getMediumLE(int var1){
        throw new UnsupportedOperationException();
    }

    public int getUnsignedMedium(int var1){
        throw new UnsupportedOperationException();
    }

    public int getUnsignedMediumLE(int var1){
        throw new UnsupportedOperationException();
    }

    public byte[] getBytes(){
        throw new UnsupportedOperationException();
    }

    public byte[] getBytes(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer getBytes(byte[] var1){
        throw new UnsupportedOperationException();
    }

    public Buffer getBytes(byte[] var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer getBytes(int var1, int var2, byte[] var3){
        throw new UnsupportedOperationException();
    }

    public Buffer getBytes(int var1, int var2, byte[] var3, int var4){
        throw new UnsupportedOperationException();
    }

    public Buffer getBuffer(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public String getString(int var1, int var2, String var3){
        throw new UnsupportedOperationException();
    }

    public String getString(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer appendBuffer(Buffer var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendBuffer(Buffer var1, int var2, int var3){
        throw new UnsupportedOperationException();
    }

    public Buffer appendBytes(byte[] var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendBytes(byte[] var1, int var2, int var3){
        throw new UnsupportedOperationException();
    }

    public Buffer appendByte(byte var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendUnsignedByte(short var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendInt(int var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendIntLE(int var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendUnsignedInt(long var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendUnsignedIntLE(long var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendMedium(int var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendMediumLE(int var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendLong(long var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendLongLE(long var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendShort(short var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendShortLE(short var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendUnsignedShort(int var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendUnsignedShortLE(int var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendFloat(float var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendDouble(double var1){
        throw new UnsupportedOperationException();
    }

    public Buffer appendString(String var1, String var2){
        throw new UnsupportedOperationException();
    }

    public Buffer appendString(String var1){
        throw new UnsupportedOperationException();
    }

    public Buffer setByte(int var1, byte var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setUnsignedByte(int var1, short var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setInt(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setIntLE(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setUnsignedInt(int var1, long var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setUnsignedIntLE(int var1, long var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setMedium(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setMediumLE(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setLong(int var1, long var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setLongLE(int var1, long var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setDouble(int var1, double var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setFloat(int var1, float var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setShort(int var1, short var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setShortLE(int var1, short var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setUnsignedShort(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setUnsignedShortLE(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setBuffer(int var1, Buffer var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setBuffer(int var1, Buffer var2, int var3, int var4){
        throw new UnsupportedOperationException();
    }

    public Buffer setBytes(int var1, ByteBuffer var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setBytes(int var1, byte[] var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setBytes(int var1, byte[] var2, int var3, int var4){
        throw new UnsupportedOperationException();
    }

    public Buffer setString(int var1, String var2){
        throw new UnsupportedOperationException();
    }

    public Buffer setString(int var1, String var2, String var3){
        throw new UnsupportedOperationException();
    }

    public int length(){
        throw new UnsupportedOperationException();
    }

    public Buffer copy(){
        throw new UnsupportedOperationException();
    }

    public Buffer slice(){
        throw new UnsupportedOperationException();
    }

    public Buffer slice(int var1, int var2){
        throw new UnsupportedOperationException();
    }

    public ByteBuf getByteBuf(){
        throw new UnsupportedOperationException();
    }

}
