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

/** stores image data samples along with associated format and size
 * metadata.
 */
public class Image
{
    /** C pointer to a zbar_symbol_t. */
    private long peer;
    private Object data;

    static
    {
        System.loadLibrary("zbarjni");
        init();
    }
    private static native void init();

    public Image ()
    {
        peer = create();
    }

    public Image (int width, int height)
    {
        this();
        setSize(width, height);
    }

    public Image (int width, int height, String format)
    {
        this();
        setSize(width, height);
        setFormat(format);
    }

    public Image (String format)
    {
        this();
        setFormat(format);
    }

    Image (long peer)
    {
        this.peer = peer;
    }

    /** Create an associated peer instance. */
    private native long create();

    protected void finalize ()
    {
        destroy();
    }

    /** Clean up native data associated with an instance. */
    public synchronized void destroy ()
    {
        if(peer != 0) {
            destroy(peer);
            peer = 0;
        }
    }

    /** Destroy the associated peer instance.  */
    private native void destroy(long peer);

    /** Image format conversion.
     * @returns a @em new image with the sample data from the original
     * image converted to the requested format fourcc.  the original
     * image is unaffected.
     */
    public Image convert (String format)
    {
        long newpeer = convert(peer, format);
        if(newpeer == 0)
            return(null);
        return(new Image(newpeer));
    }

    private native long convert(long peer, String format);

    /** Retrieve the image format fourcc. */
    public native String getFormat();

    /** Specify the fourcc image format code for image sample data. */
    public native void setFormat(String format);

    /** Retrieve a "sequence" (page/frame) number associated with this
     * image.
     */
    public native int getSequence();

    /** Associate a "sequence" (page/frame) number with this image. */
    public native void setSequence(int seq);

    /** Retrieve the width of the image. */
    public native int getWidth();

    /** Retrieve the height of the image. */
    public native int getHeight();

    /** Retrieve the size of the image. */
    public native int[] getSize();

    /** Specify the pixel size of the image. */
    public native void setSize(int width, int height);

    /** Specify the pixel size of the image. */
    public native void setSize(int[] size);

    /** Retrieve the crop region of the image. */
    public native int[] getCrop();

    /** Specify the crop region of the image. */
    public native void setCrop(int x, int y, int width, int height);

    /** Specify the crop region of the image. */
    public native void setCrop(int[] crop);

    /** Retrieve the image sample data. */
    public native byte[] getData();

    /** Specify image sample data. */
    public native void setData(byte[] data);

    /** Specify image sample data. */
    public native void setData(int[] data);

    /** Retrieve the decoded results associated with this image. */
    public SymbolSet getSymbols ()
    {
        return(new SymbolSet(getSymbols(peer)));
    }

    private native long getSymbols(long peer);

}
