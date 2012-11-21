//
// Dictionary.java
//

package net.ech.apps.anagram;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;
import net.ech.anagram.*;

/**
 * Specialized implementation of IDictionary.
 *
 * @author James Echmalian ech@ech.net
 */
public class Dictionary
    implements IDictionary
{
    private ICharacterSet charSet;
    private HashMap<LetterBag, AnagramFamily> map;
    private DictionaryIndex index;

    /**
     * Constructor.
     */
    public Dictionary (ICharacterSet charSet)
    {
        this.charSet = charSet;
        this.map = new HashMap<LetterBag, AnagramFamily> ();
    }

    /**
     * Put a word into the dictionary.
     * @return true if the word is new; false if the word was loaded previously
     */
    public boolean addWord (String word)
        throws java.text.ParseException
    {
        // Invalidate index.
        index = null;

        LetterBag key = new LetterBag (charSet);
        key.add(word);

        AnagramFamily af = map.get (key);
        if (af == null)
        {
            // New entry.
            af = new AnagramFamily(word);
            map.put (key, af);
        }
        else
        {
            for (String w : af.words)
            {
                if (w.equals(word))
                    return false;
            }
            af.addWord(word);
        }
        return true;
    }

    /**
     * Put a whole family of words into the dictionary.
     */
    public void addFamily (LetterBag bag, List<String> words)
    {
        // Invalidate index.
        index = null;

        map.put (bag, new AnagramFamily(words));
    }

    /**
     * @inheritDoc
     */
    public ICharacterSet getCharacterSet ()
    {
        return charSet;
    }

    /**
     * @inheritDoc
     */
    public IDictionaryIterator allEntries()
    {
        createIndex();
        return new DictionaryIterator (index);
    }

    /**
     * @inheritDoc
     */
    public boolean contains (LetterBag key)
    {
        return map.containsKey (key);
    }

    /**
     * @inheritDoc
     */
    public AnagramFamily getAnagramFamily (LetterBag key)
    {
        return map.get (key);
    }

    /**
     * Print the contents of the dictionary, one word per line.
     * Group anagram families together.
     */
    public void dump (PrintWriter writer)
    {
        writer.println("# grouped");

        for (LetterBag key : map.keySet())
        {
            AnagramFamily family = map.get(key);

            for (String word : family.words)
            {
                writer.println(word);
            }
        }

        writer.flush();
    }

    /**
     * Print out some dictionary stats.
     */
    public void printStats (PrintWriter writer)
    {
        int anagramEntryCount = 0;
        int anagramWordCount = 0;

        createIndex();

        for (int size = 1; size <= index.getMaxSize(); ++size)
        {
            LetterBag[] keys = index.getBucket(size);

            if (keys.length > 0)
            {
                int bucketWordCount = 0;
                int bucketEntryCount = keys.length;
                for (int i = 0; i < keys.length; ++i)
                {
                    bucketWordCount += map.get(keys[i]).words.length;
                }
                anagramEntryCount += bucketEntryCount;
                anagramWordCount += bucketWordCount;

                writer.println ("[" + size + "]: words: " + bucketWordCount + "  entries: " + bucketEntryCount);
            }
        }

        writer.println ();

        DictionaryIterator iter = new DictionaryIterator(index);
        iter.setMaxSize(1);
        iter.setMinSize(1);
        LetterBag bag;
        while ((bag = iter.current()) != null)
        {
            writer.println (bag);
            iter.next();
        }

        writer.println ();
        writer.println ("Number of dictionary entries: " + map.size());
        writer.println ("Number of indexed words: " + anagramWordCount);
        writer.println ("Number of index entries: " + anagramEntryCount);
        writer.flush ();
    }

    /**
     * @private
     */
    private void createIndex ()
    {
        if (index == null)
        {
            index = new DictionaryIndex(allKeys());
        }
    }

    /**
     * @private
     */
    private LetterBag[] allKeys()
    {
        LetterBag[] keys = new LetterBag [map.keySet().size()];

        int i = 0;
        for (Iterator<LetterBag> k = map.keySet().iterator(); k.hasNext(); )
        {
            keys[i++] = k.next();
        }

        return keys;
    }
}
