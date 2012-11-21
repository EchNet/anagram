//
// IDictionary.java
//

package net.ech.anagram;

/**
 * An IDictionary is a source of words for an anagram finder.  It provides
 * fast word validation and fast lookup of the anagram family of which a
 * given word is a member.  Effectively, it is a map of LetterBag to 
 * IAnagramFamily.
 * 
 * An IDictionary has an associated ICharacterSet.
 */
public interface IDictionary
{
    /**
     * Get the character set associated with this dictionary.
     */
    public ICharacterSet getCharacterSet();

    /**
     * Get an iterator initially set to traverse all dictionary entries,
     * sorted in order of descending size.
     */
    public IDictionaryIterator allEntries();

    /**
     * Validate the given letter combination.
     * @return true if some anagram of the letter combination is a word
     */
    public boolean contains(LetterBag input);

    /**
     * Find the family of anagrams identified by the given letter combination.
     * @return an anagram family, or null if the input is invalid
     */
    public IAnagramFamily getAnagramFamily(LetterBag input);
}
