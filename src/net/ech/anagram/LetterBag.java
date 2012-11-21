//
// LetterBag.java
//

package net.ech.anagram;

import java.text.ParseException;

/**
 * A LetterBag is an unordered collection of letter values and word breaks.
 * LetterBags may be compared for equality, added to and subtracted from 
 * each other, and used as a hash key.  LetterBags represent letter values
 * as character set-relative ordinals.  A LetterBag is associated with the 
 * character set from which it was constructed, and is incompatible with any
 * LetterBag constructed from a different character set.
 */
public final class LetterBag
    implements java.io.Serializable
{
    // Index zero is the character count.
    // Index one is the word break count.
    // Indexes 2..* are letter counts.

    private short[] counts;
    private ICharacterSet characterSet;

    /**
     * Constructor.
     * @param characterSet   the active character set
     */
    public LetterBag (ICharacterSet characterSet)
    {
        this.characterSet = characterSet;
        clear();
    }

    /**
     * Constructor.
     * @param characterSet   the active character set
     */
    public LetterBag (ICharacterSet characterSet, String word)
        throws ParseException
    {
        this(characterSet);
        add(word);
    }

    /**
     * Make this LetterBag empty.
     */
    public void clear()
    {
        this.counts = new short [characterSet.getCardinality () + 2];
    }

    /** 
     * Add a String to this LetterBag.
     * @param word   a string that may contain only letters and legal punctuation
     * @throws ParseException if the string contains an illegal character or is too long
     */
    public void add (String word)
        throws ParseException
    {
        if (word.length() > 100)
        {
            throw new ParseException ("too long", 100);
        }

        for (int i = 0; i < word.length(); ++i)
        {
            try
            {
                int ord = characterSet.getOrdinal (word.charAt(i));
                if (ord >= 0)
                {
                    counts[ord + 2] += 1;
                    counts[0] += 1;
                }
            }
            catch (ParseException e)
            {
                throw new ParseException (e.getMessage(), i);
            }
        }
    }

    /**
     * Add that LetterBag to this one, modifying this one.
     */
    public void add (LetterBag that)
    {
        if (that.characterSet != this.characterSet)
        {
            throw new RuntimeException ("mixed character sets?");
        }

        for (int i = 0; i < counts.length; ++i)
        {
            counts[i] += that.counts[i];
        }
    }

    /**
     * Add that LetterBag to this one, modifying this one.  Treat that
     * LetterBag as an additional word.
     */
    public void addWord (LetterBag that)
    {
        boolean wasEmpty = counts[0] == 0;
        add (that);
        if (!wasEmpty)
        {
            counts[1] += 1;
        }
    }

    /**
     * Subtract that LetterBag from this one, modifying this one.
     * If this LetterBag does not contain that LetterBag, this LetterBag
     * becomes invalid.
     */
    public void subtract (LetterBag that)
    {
        if (that.characterSet != this.characterSet)
        {
            throw new RuntimeException ("mixed character sets?");
        }

        for (int i = 0; i < counts.length; ++i)
        {
            counts[i] -= that.counts[i];
        }
    }

    /**
     * Subtract that LetterBag from this one, modifying this one.
     * Treat that LetterBag as a word.
     */
    public void subtractWord (LetterBag that)
    {
        subtract (that);
        counts[1] -= 1;
    }

    /**
     * Get the number of characters in this bag.
     */
    public int getSize ()
    {
        return counts[0];
    }

    /**
     * Get the number of words in this bag.
     */
    public int getWordCount ()
    {
        return getSize() == 0 ? 0 : (counts[1] + 1);
    }

    /**
     * Format as string.
     */
    public String toString ()
    {
        StringBuffer buf = new StringBuffer ();

        buf.append ('[');

        for (int j = 0; j < counts[1]; ++j)
        {
            buf.append (' ');
        }

        for (int i = 2; i < counts.length; ++i)
        {
            for (int j = 0; j < counts[i]; ++j)
            {
                buf.append (characterSet.getRepresentative (i - 2));
            }
        }

        buf.append (']');

        return buf.toString ();
    }

    /**
     * Hash.
     */
    public int hashCode ()
    {
        int h = 0;

        for (int i = 1; i < counts.length; ++i)
        {
            h = (31 * h) + ((i + 1) * counts[i]);
        }

        return h;
    }

    /**
     * Equality.
     */
    public boolean equals (Object that)
    {
        try
        {
            return equalz((LetterBag) that);
        }
        catch (Exception e)
        {
        }

        return false;
    }

    /**
     * Equality.
     */
    public boolean equalz (LetterBag that)
    {
        if (this.characterSet != that.characterSet)
        {
            return false;
        }

        for (int i = 0; i < counts.length; ++i)
        {
            if (this.counts[i] != that.counts[i])
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if that bag is a proper subbag of this one.
     */
    public boolean contains (LetterBag that)
    {
        if (this.characterSet != that.characterSet)
        {
            return false;
        }

        for (int i = 0; i < counts.length; ++i)
        {
            if (that.counts[i] > this.counts[i])
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Create a copy of this LetterBag.
     * Never mind clone().
     */
    public LetterBag copy ()
    {
        LetterBag that = new LetterBag (characterSet);
        that.add (this);
        return that;
    }
}
