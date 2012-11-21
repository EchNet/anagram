//
// AnagramCollector.java
//

package net.ech.apps.anagram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.ech.anagram.*;

/**
 * An IResultConsumer that buffers its results for later access. 
 */
public class AnagramCollector
    implements IResultConsumer
{
    private int limit = -1;
    private boolean limitExceeded = false;
    private List<String[]> list = new ArrayList<String[]>();

    /**
     * Constructor.
     */
    public AnagramCollector ()
    {
    }

    /**
     * Constructor.
     */
    public AnagramCollector (int limit)
    {
        this.limit = limit;
    }

    /**
     * Limit property.
     */
    public int getLimit()
    {
        return this.limit;
    }

    /**
     * Limit property.
     */
    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    /**
     * Size property.
     */
    public int getSize()
    {
        return list.size();
    }

    /**
     * LimitExceeded property.
     */
    public boolean isLimitExceeded()
    {
        return limitExceeded;
    }

    /**
     * Get iterator.
     */
    public Iterator<String[]> iterator()
    {
        return list.iterator();
    }

    /**
     * @inheritDoc
     */
    public void consume (IAnagramFamily[] result)
        throws LongJump
    {
        new Visitor(result).run();
    }

    /**
     * Visitor class.
     */
    private class Visitor
    {
        private IAnagramFamily[] result;
        private List<String> buf = new ArrayList<String>();

        /**
         * Constructor.
         */
        public Visitor (IAnagramFamily[] result)
        {
            this.result = result;
        }

        /**
         * Visit all combos.
         */
        public void run()
            throws LongJump
        {
            visitCombos (0);
        }

        private void visitCombos (int startIndex)
            throws LongJump
        {
            if (startIndex >= result.length)
            {
                if (limit >= 0 && list.size() >= limit)
                {
                    limitExceeded = true;
                    throw new LongJump();
                }
                list.add ((String[]) buf.toArray(new String[buf.size()]));
            }
            else
            {
                List<String> anagrams = result[startIndex].getWords();
                for (int i = 0; i < anagrams.size(); ++i)
                {
                    buf.add (anagrams.get(i));
                    visitCombos (startIndex + 1);
                    buf.remove (buf.size() - 1);
                }
            }
        }
    }
}
