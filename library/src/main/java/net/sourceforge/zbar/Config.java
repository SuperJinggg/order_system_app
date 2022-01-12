/*
 *    Copyright (C) 2007-2011 Jeff Brown
 *    Copyright 2021 Institute of Software Chinese Academy of Sciences, ISRC

 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.sourceforge.zbar;

/** Decoder configuration options.
 */
public class Config
{
    /** Enable symbology/feature. */
    public static final int ENABLE = 0;
    /** Enable check digit when optional. */
    public static final int ADD_CHECK = 1;
    /** Return check digit when present. */
    public static final int EMIT_CHECK = 2;
    /** Enable full ASCII character set. */
    public static final int ASCII = 3;

    /** Minimum data length for valid decode. */
    public static final int MIN_LEN = 0x20;
    /** Maximum data length for valid decode. */
    public static final int MAX_LEN = 0x21;

    /** Required video consistency frames. */
    public static final int UNCERTAINTY = 0x40;

    /** Enable scanner to collect position data. */
    public static final int POSITION = 0x80;

    /** Image scanner vertical scan density. */
    public static final int X_DENSITY = 0x100;
    /** Image scanner horizontal scan density. */
    public static final int Y_DENSITY = 0x101;
}
