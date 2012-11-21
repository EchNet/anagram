//
// IDictionaryIterator.java
//

package net.ech.anagram;

/**
 * An IDictionaryIterator traverses an IDictionary.
 */
public interface IDictionaryIterator
{
    /**
     * Restrict this iterator to visiting entries of no smaller than the
     * given size.
     */
    public void setMinSize(int minSize);

    /**
     * Restrict this iterator to visiting entries of no larger than the
     * given size.
     */
    public void setMaxSize(int maxSize);

    /**
     * Get the current entry.
     * @return the current dictionary key, or null if there are no more
     */
    public LetterBag current();

    /**
     * Advance to the next entry.
     */
    public void next();

    /**
     * Make a copy of this iterator.
     */
    public IDictionaryIterator copy();
}
