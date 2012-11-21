//
// DictionaryIterator.java
//

package net.ech.apps.anagram;

import net.ech.anagram.*;

/**
 * Iterator for Dictionary.
 *
 * @author James Echmalian ech@ech.net
 */
public class DictionaryIterator
    implements IDictionaryIterator
{
    private DictionaryIndex index;
    private int minSize;
    private int maxSize;
    private int currentSize;
    private LetterBag[] currentArray;
    private int currentIndex;

    /**
     * Constructor.
     */
    private DictionaryIterator ()
    {
    }

    /**
     * Constructor.
     */
    public DictionaryIterator (DictionaryIndex index)
    {
        this.index = index;
        this.minSize = 1;
        this.currentSize = this.maxSize = index.getMaxSize();
    }

    /**
     * @inheritDoc
     */
    public void setMinSize(int minSize)
    {
        this.minSize = Math.max(minSize, this.minSize);
    }

    /**
     * @inheritDoc
     */
    public void setMaxSize(int maxSize)
    {
        this.maxSize = Math.min(maxSize, this.maxSize);
    }

    /**
     * @inheritDoc
     */
    public LetterBag current()
    {
        return visit (false);
    }

    /**
     * @inheritDoc
     */
    public void next()
    {
        visit (true);
    }

    /**
     * @private
     */
    private LetterBag visit(boolean advance)
    {
        if (currentSize > maxSize)
        {
            currentSize = maxSize;
            currentArray = null;
        }

        while (currentSize >= minSize)
        {
            if (currentArray == null)
            {
                currentArray = index.getBucket(currentSize);
                currentIndex = 0;
            }

            if (currentIndex >= currentArray.length)
            {
                --currentSize;
                currentArray = null;
            }
            else if (advance)
            {
                ++currentIndex;
                break;
            }
            else
            {
                return currentArray[currentIndex];
            }
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    public IDictionaryIterator copy()
    {
        DictionaryIterator copy = new DictionaryIterator();
        copy.index = this.index;
        copy.minSize = this.minSize;
        copy.maxSize = this.maxSize;
        copy.currentSize = this.currentSize;
        copy.currentArray = this.currentArray;
        copy.currentIndex = this.currentIndex;
        return copy;
    }
}

