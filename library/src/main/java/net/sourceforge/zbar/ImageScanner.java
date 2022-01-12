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

/** Read barcodes from 2-D images.
 */
public class ImageScanner
{
    /** C pointer to a zbar_image_scanner_t. */
    private long peer;

    static
    {
        System.loadLibrary("zbarjni");
        init();
    }
    private static native void init();

    public ImageScanner ()
    {
        peer = create();
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

    /** Set config for indicated symbology (0 for all) to specified value.
     */
    public native void setConfig(int symbology, int config, int value)
        throws IllegalArgumentException;

    /** Parse configuration string and apply to image scanner. */
    public native void parseConfig(String config);

    /** Enable or disable the inter-image result cache (default disabled).
     * Mostly useful for scanning video frames, the cache filters duplicate
     * results from consecutive images, while adding some consistency
     * checking and hysteresis to the results.  Invoking this method also
     * clears the cache.
     */
    public native void enableCache(boolean enable);

    /** Retrieve decode results for last scanned image.
     * @returns the SymbolSet result container
     */
    public SymbolSet getResults ()
    {
        return(new SymbolSet(getResults(peer)));
    }

    private native long getResults(long peer);

    /** Scan for symbols in provided Image.
     * The image format must currently be "Y800" or "GRAY".
     * @returns the number of symbols successfully decoded from the image.
     */
    public native int scanImage(Image image);
}
