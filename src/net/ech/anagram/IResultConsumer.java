//
// IResultConsumer.java
//

package net.ech.anagram;

/**
 * An interface for an object that accepts an anagrammer result.
 */
public interface IResultConsumer
{
    /**
     * Consume a single entry in a stream of Anagrammer results.
     * The parameter is an array of anagram families.   Each of the
     * combinations of all entries of each of the anagram families is an
     * anagram of the original input.  
     * @param the array of anagrams
     * @throws net.ech.anagram.LongJump  to abort the anagram process
     */
    public void consume (IAnagramFamily[] result)
        throws LongJump;
}
