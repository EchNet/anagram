//
// DictionaryIndex.java
//

package net.ech.apps.anagram;

import java.util.*;
import net.ech.anagram.*;

/**
 * Index structure for Dictionary.
 *
 * @author James Echmalian ech@ech.net
 */
public class DictionaryIndex
{
    private Map<Integer, LetterBag[]> map = new HashMap<Integer, LetterBag[]> ();
    private int maxSize;

    /**
     * Constructor.
     * @param keys   array of all dictionary keys - modified by this function
     */
    public DictionaryIndex (LetterBag[] keys)
    {
        sortKeys(keys);

        int start = 0;
        int priorSize = 0;

        for (int i = 0; i < keys.length; ++i)
        {
            int currentSize = keys[i].getSize();
            if (currentSize != priorSize && priorSize > 0)
            {
                stowSubrange (keys, start, i);
                start = i;
            }
            priorSize = currentSize;
        }

        if (start > 0)
        {
            stowSubrange (keys, start, keys.length);
        }

        if (keys.length > 0)
        {
            this.maxSize = keys[0].getSize();
        }
    }

    /**
     * Get the size of the largest dictionary key.
     */
    public int getMaxSize()
    {
        return maxSize;
    }

    /**
     * This type of dictionary index buckets entries by size.
     */
    public LetterBag[] getBucket(int size)
    {
        LetterBag[] keys = map.get(new Integer(size));
        return keys == null ? new LetterBag[0] : keys;
    }

    /**
     * Sort the keys.
     */
    private void sortKeys(LetterBag[] keys)
    {
        // Sort on word length.
        Arrays.sort(keys, new Comparator<LetterBag>()
        {
            public int compare (LetterBag o1, LetterBag o2)
            {
                return o2.getSize() - o1.getSize();
            }

            public boolean equals (Object obj)
            {
                return false;
            }
        });
    }

    /**
     * @private
     */
    private void stowSubrange(LetterBag[] keys, int start, int end)
    {
        LetterBag[] subkeys = new LetterBag [end - start];

        for (int i = 0; i < subkeys.length; ++i)
        {
            subkeys[i] = keys[start + i];
        }

        stow(subkeys[0].getSize(), subkeys);
    }

    /**
     * @private
     */
    private void stow(int size, LetterBag[] keys)
    {
        map.put(new Integer(size), keys);
    }
}

