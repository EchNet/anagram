//
// Anagrammer.java
//

package net.ech.anagram;

import java.util.*;

/**
 * Anagrammer is a multi-word anagran finder.
 *
 * @author James Echmalian ech@ech.net
 */
public class Anagrammer
{
    private IDictionary dict;

    /**
     * Digest the given input string into a form usable by findAnagrams().
     */
    public static LetterBag digestInput(String input, ICharacterSet charSet)
        throws java.text.ParseException
    {
        LetterBag inputBag = new LetterBag(charSet);

        StringTokenizer toks = new StringTokenizer (input);
        while (toks.hasMoreTokens())
        {
            LetterBag wordBag = new LetterBag(charSet);
            wordBag.add (toks.nextToken());
            inputBag.addWord (wordBag);
        }

        return inputBag;
    }

    /**
     * Constructor.
     */
    public Anagrammer (IDictionary dict)
    {
        this.dict = dict;
    }

    /**
     * Get the dictionary used by this anagrammer.
     */
    public IDictionary getDictionary()
    {
        return dict;
    }

    /**
     * Find all the anagrams of the given input string.  Pass each one
     * to the consumer.
     * @param input    the digested input
     * @param consumer a callback to receive the results.  May not be null
     */
    public void findAnagrams (LetterBag input, IResultConsumer consumer)
    {
        new AnagramProcess (input, consumer).run();
    }

    //
    // Inner class that maintains process state and does all the work.
    //
    private class AnagramProcess implements Runnable
    {
        private final LetterBag input;
        private final IResultConsumer consumer;

        private ArrayList<LetterBag> matchStack;
        private LetterBag unmatched;
        private HashSet<LetterBag> priorTailMatches;

        /**
         * Constructor.
         */
        public AnagramProcess (LetterBag input, IResultConsumer consumer)
        {
            this.input = input;
            this.consumer = consumer;
        }

        /**
         * Entry point.
         */
        public void run ()
        {
            try
            {
                find();
            }
            catch (LongJump e)
            {
            }
        }

        /**
         * Run the finder from the current state, using the whole 
         * dictionary.
         */
        private void find ()
            throws LongJump
        {
            // Start over.
            this.matchStack = new ArrayList<LetterBag> ();
            this.unmatched = input;
            this.priorTailMatches = new HashSet<LetterBag> ();

            find (dict.allEntries());
        }

        /**
         * Run the finder from the current state, using the portion
         * of the dictionary given.
         */
        private void find (IDictionaryIterator iter)
            throws LongJump
        {
            // Start with a straight lookup.  Unless there are multi-word
            // dictionary entries, this will only return results in the case
            // that the unmatched set is a single word.
            //
            if (dict.contains (unmatched))
            {
                LetterBag copy = unmatched.copy ();
                pushMatch (copy);
                consumeResults ();
                popMatch ();
                priorTailMatches.add (copy);
            }

            multifind (iter);
        }

        /**
         * Find multi-word anagrams, recursively.
         */
        private void multifind (IDictionaryIterator iter)
            throws LongJump
        {
            int wordCount = unmatched.getWordCount();
            if (wordCount > 1)
            {
                // Try matching all dictionary words, starting
                // with largest possible.  Never backtrack - otherwise multiple
                // combinations of a single word set will be emitted.  
                // (e.g., both "ran machinelike" and "machinelike ran")

                int inputSize = unmatched.getSize();
                int maxSize = inputSize - wordCount + 1;
                int minSize = (inputSize + wordCount - 1) / wordCount;
                int halfSize = (wordCount == 2 && (inputSize % 2) == 0) ? (inputSize / 2) : -1;  // see below.

                // Optimization: skip words that are too long or too short.
                iter.setMaxSize (maxSize);
                iter.setMinSize (minSize);

                //
                // Each iteration through the following loop finds ALL
                // of the multi-word anagrams that include the current
                // anagram family.
                //
                LetterBag current;
                while ((current = iter.current()) != null)
                {
                    if (current.getSize() == halfSize && 
                        priorTailMatches.contains(current))
                    {
                        // Skip.  This takes care of the dupe prevention
                        // case that backtrack avoidance alone won't 
                        // handle - two words of equal size.  While
                        // in this situation, must avoid visiting words
                        // that have matched the right hand side fully.
                    }
                    else if (unmatched.contains (current))
                    {
                        // RECURSION: find all the anagrams of the remaining
                        // unmatched portion, if any.

                        pushMatch (current);
                        unmatched.subtractWord (current);

                        // The recursive activation works with a COPY of 
                        // the iterator.  Must not backtrack here.
                        find (iter.copy());

                        // UN-recurse.
                        unmatched.addWord (current);
                        popMatch ();
                    }

                    iter.next();
                }
            }
        }

        private void pushMatch (LetterBag match)
        {
            matchStack.add (match);
        }

        private void popMatch ()
        {
            matchStack.remove (matchStack.size() - 1);
        }

        private void consumeResults ()
            throws LongJump
        {
            IAnagramFamily[] result = new IAnagramFamily[matchStack.size()];

            for (int i = 0; i < result.length; ++i)
            {
                result[i] = dict.getAnagramFamily (matchStack.get (i));
            }

            consumer.consume(result);
        }
    }
}
