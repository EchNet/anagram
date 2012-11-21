//
// ResultWriter.java
//

package net.ech.apps.anagram;

import java.util.List;
import java.io.PrintWriter;
import net.ech.anagram.*;

/**
 * An IResultConsumer that prints its results to a PrintWriter.
 */
public class ResultWriter
    implements IResultConsumer
{
    private PrintWriter writer;

    /**
     * Constructor.
     * @param writer a PrintWriter.
     */
    public ResultWriter (PrintWriter writer)
    {
        this.writer = writer;
    }

    /**
     * @inheritDoc
     */
    public void consume (IAnagramFamily[] result)
    {
        new Printer(result).run();
    }

    /**
     * Worker class.
     */
    private class Printer
    {
        private IAnagramFamily[] result;
        private StringBuffer buf;

        /**
         * Constructor
         */
        public Printer (IAnagramFamily[] result)
        {
            this.result = result;
            this.buf = new StringBuffer();
        }

        /**
         * Print all combos.
         */
        public void run()
        {
            printAllCombos (0);
        }

        private void printAllCombos (int startIndex)
        {
            if (startIndex >= result.length)
            {
                writer.println (buf.toString());
                return;
            }

            if (startIndex > 0)
            {
                buf.append (' ');
            }

            List<String> anagrams = result[startIndex].getWords();
            for (int i = 0; i < anagrams.size(); ++i)
            {
                int priorLength = buf.length ();
                buf.append (anagrams.get(i));

                printAllCombos (startIndex + 1);

                buf.setLength (priorLength);
            }

            if (startIndex > 0)
            {
                buf.setLength (buf.length () - 1);
            }

            writer.flush();
        }
    }
}
