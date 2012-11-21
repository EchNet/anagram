//
// IAnagramFamily.java
//

package net.ech.anagram;

/**
 * An IAnagramFamily is a list of words that are anagrams of each other.
 */
public interface IAnagramFamily
{
    /**
     * Get the parent dictionary.
     */
    public IDictionary getDictionary();

    /**
     * Get the list of words associated by the anagram relationship.
     * This list is read-only.
     */
    public java.util.List<String> getWords();
}
