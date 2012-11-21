//
// LowerCaseAscii.java
//

package net.ech.anagram.csets;

import java.text.ParseException;
import net.ech.anagram.ICharacterSet;

/**
 * A non-configurable ICharacterSet that understands only English letters
 * and limited punctuation.
 */
public class LowerCaseAscii implements ICharacterSet
{
    private final static char MIN_LOWER = 'a';
    private final static char MAX_LOWER = 'z';
 
    private final static char MIN_UPPER = 'A';
    private final static char MAX_UPPER = 'Z';

    private final static String PUNCT = "'-";

    /**
     * @inheritDoc
     */
    public int getCardinality ()
    {
        return MAX_LOWER - MIN_LOWER + 1;  // 26
    }

    /**
     * @inheritDoc
     */
    public int getOrdinal (char c)
        throws ParseException
    {
        if (c >= MIN_LOWER && c <= MAX_LOWER) return c - MIN_LOWER;
        if (c >= MIN_UPPER && c <= MAX_UPPER) return c - MIN_UPPER;
        if (PUNCT.indexOf(c) >= 0) return -1;
        throw new ParseException("Invalid character: '" + c + "' (#" + Integer.toHexString(c) + ")", 0);
    }
    
    /**
     * @inheritDoc
     */
    public char getRepresentative (int index)
        throws IllegalArgumentException
    {
        return (char) (MIN_LOWER + index);
    }
}
