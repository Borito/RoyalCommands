/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility code for dealing with different endian systems.
 * <p/>
 * Different computer architectures adopt different conventions for
 * byte ordering. In so-called "Little Endian" architectures (eg Intel),
 * the low-order byte is stored in memory at the lowest address, and
 * subsequent bytes at higher addresses. For "Big Endian" architectures
 * (eg Motorola), the situation is reversed.
 * This class helps you solve this incompatability.
 * <p/>
 * Origin of code: Excalibur
 *
 * @version $Id: EndianUtils.java 1302056 2012-03-18 03:03:38Z ggregory $
 * @see org.apache.commons.io.input.SwappedDataInputStream
 */
public class EndianUtils {

    /**
     * Instances should NOT be constructed in standard programming.
     */
    public EndianUtils() {
        super();
    }

    // ========================================== Swapping routines

    // ========================================== Swapping read/write routines

    /**
     * Reads a "long" value from a byte array at a given offset. The value is
     * converted to the opposed endian system while reading.
     *
     * @param data   source byte array
     * @param offset starting offset in the byte array
     * @return the value read
     */
    public static long readSwappedLong(byte[] data, int offset) {
        long low =
                ((data[offset] & 0xff)) +
                        ((data[offset + 1] & 0xff) << 8) +
                        ((data[offset + 2] & 0xff) << 16) +
                        ((data[offset + 3] & 0xff) << 24);
        long high =
                ((data[offset + 4] & 0xff)) +
                        ((data[offset + 5] & 0xff) << 8) +
                        ((data[offset + 6] & 0xff) << 16) +
                        ((data[offset + 7] & 0xff) << 24);
        return (high << 32) + (0xffffffffL & low);
    }

    /**
     * Reads a "short" value from an InputStream. The value is
     * converted to the opposed endian system while reading.
     *
     * @param input source InputStream
     * @return the value just read
     * @throws IOException in case of an I/O problem
     */
    public static short readSwappedShort(InputStream input)
            throws IOException {
        return (short) (((read(input) & 0xff)) +
                ((read(input) & 0xff) << 8));
    }

    /**
     * Reads a unsigned short (16-bit) from an InputStream. The value is
     * converted to the opposed endian system while reading.
     *
     * @param input source InputStream
     * @return the value just read
     * @throws IOException in case of an I/O problem
     */
    public static int readSwappedUnsignedShort(InputStream input)
            throws IOException {
        int value1 = read(input);
        int value2 = read(input);

        return (((value1 & 0xff)) +
                ((value2 & 0xff) << 8));
    }

    /**
     * Reads a "int" value from an InputStream. The value is
     * converted to the opposed endian system while reading.
     *
     * @param input source InputStream
     * @return the value just read
     * @throws IOException in case of an I/O problem
     */
    public static int readSwappedInteger(InputStream input)
            throws IOException {
        int value1 = read(input);
        int value2 = read(input);
        int value3 = read(input);
        int value4 = read(input);

        return ((value1 & 0xff)) +
                ((value2 & 0xff) << 8) +
                ((value3 & 0xff) << 16) +
                ((value4 & 0xff) << 24);
    }

    /**
     * Reads a "long" value from an InputStream. The value is
     * converted to the opposed endian system while reading.
     *
     * @param input source InputStream
     * @return the value just read
     * @throws IOException in case of an I/O problem
     */
    public static long readSwappedLong(InputStream input)
            throws IOException {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) read(input);
        }
        return readSwappedLong(bytes, 0);
    }

    /**
     * Reads a "float" value from an InputStream. The value is
     * converted to the opposed endian system while reading.
     *
     * @param input source InputStream
     * @return the value just read
     * @throws IOException in case of an I/O problem
     */
    public static float readSwappedFloat(InputStream input)
            throws IOException {
        return Float.intBitsToFloat(readSwappedInteger(input));
    }

    /**
     * Reads a "double" value from an InputStream. The value is
     * converted to the opposed endian system while reading.
     *
     * @param input source InputStream
     * @return the value just read
     * @throws IOException in case of an I/O problem
     */
    public static double readSwappedDouble(InputStream input)
            throws IOException {
        return Double.longBitsToDouble(readSwappedLong(input));
    }

    /**
     * Reads the next byte from the input stream.
     *
     * @param input the stream
     * @return the byte
     * @throws IOException if the end of file is reached
     */
    private static int read(InputStream input)
            throws IOException {
        int value = input.read();

        if (-1 == value) {
            throw new EOFException("Unexpected EOF reached");
        }

        return value;
    }
}
