//
// AnagramFamily.java
//

package net.ech.apps.anagram;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;
import net.ech.anagram.*;

/**
 * Specialized implementation of IAnagramFamily.  Value type for 
 * Dictionary.
 *
 * @author James Echmalian ech@ech.net
 */
public class AnagramFamily
    implements IAnagramFamily
{
    private Dictionary dict;
    String[] words;

    /**
     * C'tor.
     */
    public AnagramFamily(Dictionary dict)
    {
        this.dict = dict;
        words = new String[0];
    }

    /**
     * C'tor.
     */
    public AnagramFamily(String word)
    {
        this.dict = dict;
        words = new String[] { word };
    }

    /**
     * C'tor.
     */
    public AnagramFamily(List<String> wlist)
    {
        this.dict = dict;
        words = new String[wlist.size()];
        for (int i = 0; i < words.length; ++i)
        {
            words[i] = wlist.get(i);
        }
    }

    /**
     * Add a word.
     */
    public void addWord(String word)
    {
        String[] oldWords = words;
        words = new String [oldWords.length + 1];
        for (int i = 0; i < oldWords.length; ++i)
        {
            words[i] = oldWords[i];
        }
        words[oldWords.length] = word;
    }

    /**
     * Get the parent dictionary.
     */
    public IDictionary getDictionary()
    {
        return dict;
    }

    /**
     * Get the list of words associated by the anagram relationship.
     * This list is read-only.
     */
    public List<String> getWords()
    {
        return new AbstractList<String> ()
        {
            public String get(int i)
            {
                return words[i];
            }
            public int size()
            {
                return words.length;
            }
        };
    }
}
