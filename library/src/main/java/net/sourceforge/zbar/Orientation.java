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

/** Decoded symbol coarse orientation.
 */
public class Orientation
{
    /** Unable to determine orientation. */
    public static final int UNKNOWN = -1;
    /** Upright, read left to right. */
    public static final int UP = 0;
    /** sideways, read top to bottom */
    public static final int RIGHT = 1;
    /** upside-down, read right to left */
    public static final int DOWN = 2;
    /** sideways, read bottom to top */
    public static final int LEFT = 3;
}
