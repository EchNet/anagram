//
// ICharacterSet.java
//

package net.ech.anagram;

import java.text.ParseException;

/**
 * Anagrammer configuration.
 *
 * An ICharacterSet is a reduction of the char type, used in parsing input
 * strings to the Anagrammer.  An ICharacterSet classifies each char value
 * as one of the following:
 * <li>a letter</li>
 * <li>punctuation</li>
 * <li>invalid</li>
 *
 * Furthermore, an ICharacterSet maps each Letter char value to a zero-based
 * ordinal value.  All char values having the same ordinal value are treated
 * as equals for the sake of anagramming.  For example, a typically configured
 * Anagrammer treats lower case 'a' and upper case 'A' as one character.
 */
public interface ICharacterSet
    extends java.io.Serializable
{
    /**
     * Get the number of ordinal values in this set.
     */
    public int getCardinality ();

    /**
     * Get the ordinal value of the given character.
     * @return the ordinal value, if the character is a letter; or -1
     * if the character is punctuation
     * @throws ParseException if the character is invalid
     */
    public int getOrdinal (char c)
        throws ParseException;
    
    /**
     * Get a character having the given ordinal value.  (More than one
     * character may have the ordinal value)
     * @param ordinal  an ordinal value
     * @throw IllegalArgumentException if the ordinal value is out of range
     */
    public char getRepresentative (int ordinal)
        throws IllegalArgumentException;
}
