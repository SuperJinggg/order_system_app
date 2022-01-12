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

/** Iterator over a SymbolSet.
 */
public class SymbolIterator
    implements java.util.Iterator<Symbol>
{
    /** Next symbol to be returned by the iterator. */
    private Symbol current;

    /** SymbolIterators are only created by internal interface methods. */
    SymbolIterator (Symbol first)
    {
        current = first;
    }

    /** Returns true if the iteration has more elements. */
    public boolean hasNext ()
    {
        return(current != null);
    }

    /** Retrieves the next element in the iteration. */
    public Symbol next ()
    {
        if(current == null)
            throw(new java.util.NoSuchElementException
                  ("access past end of SymbolIterator"));

        Symbol result = current;
        long sym = current.next();
        if(sym != 0)
            current = new Symbol(sym);
        else
            current = null;
        return(result);
    }

    /** Raises UnsupportedOperationException. */
    public void remove ()
    {
        throw(new UnsupportedOperationException
              ("SymbolIterator is immutable"));
    }
}
