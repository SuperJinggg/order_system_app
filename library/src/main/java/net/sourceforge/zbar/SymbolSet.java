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

/** Immutable container for decoded result symbols associated with an image
 * or a composite symbol.
 */
public class SymbolSet
    extends java.util.AbstractCollection<Symbol>
{
    /** C pointer to a zbar_symbol_set_t. */
    private long peer;

    static
    {
        System.loadLibrary("zbarjni");
        init();
    }
    private static native void init();

    /** SymbolSets are only created by other package methods. */
    SymbolSet (long peer)
    {
        this.peer = peer;
    }

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

    /** Release the associated peer instance.  */
    private native void destroy(long peer);

    /** Retrieve an iterator over the Symbol elements in this collection. */
    public java.util.Iterator<Symbol> iterator ()
    {
        long sym = firstSymbol(peer);
        if(sym == 0)
            return(new SymbolIterator(null));

        return(new SymbolIterator(new Symbol(sym)));
    }

    /** Retrieve the number of elements in the collection. */
    public native int size();

    /** Retrieve C pointer to first symbol in the set. */
    private native long firstSymbol(long peer);
}
