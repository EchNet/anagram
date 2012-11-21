//
// Main.java
//

package net.ech.apps.anagram;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import net.ech.anagram.*;
import net.ech.anagram.csets.LowerCaseAscii;

/**
 * Run the anagram finder as a console app.
 *
 * @author James Echmalian ech@ech.net
 */
public class Main
{
    // Command line options.
    private List<String> dictList = new ArrayList<String>();
    private List<String> inputWordList = new ArrayList<String>();
    private boolean loadDefault = true;
    private boolean doPrintStats = false;
    private boolean doDump = false;

    // App state.
    private IDictionary dict;
    private PrintWriter output;

    /**
     * Main program to drive the AnagramVisitor class.
     * Runs in interactive mode or batch mode.
     */
    public static void main (String[] args)
    {
        try
        {
            Main app = new Main ();
            app.parseArgs (args);
            app.run ();
        }
        catch (Exception e)
        {
            e.printStackTrace (System.err);
            System.exit (1);
        }
    }

    /**
     * Constructor.
     */
    public Main()
    {
        output = new PrintWriter (new OutputStreamWriter (System.out));
    }

    public void parseArgs(String[] args)
        throws Exception
    {
        for (int i = 0; i < args.length; ++i)
        {
            if (args[i].startsWith ("-"))
            {
                if (args[i].equals("--words") || args[i].equals ("-w"))
                {
                    ++i;
                    dictList.add(args[i]);
                }
                else if (args[i].equals("--stats") || args[i].equals ("-s"))
                {
                    doPrintStats = true;
                }
                else if (args[i].equals("--dump") || args[i].equals ("-d"))
                {
                    doDump = true;
                }
                else
                {
                    usage ();
                    System.exit (1);
                }
            }
            else
            {
                inputWordList.add(args[i]);
            }
        }
    }

    private void run ()
        throws Exception
    {
        Dictionary d = new Dictionary(new LowerCaseAscii ());
        dict = d;

        if (dictList.size() > 0)
        {
            // Load other specified dictionaries.
            for (int i = 0; i < dictList.size(); ++i)
            {
                WordList.loadWords ((String) dictList.get(i), d);
            }
        }
        else
        {
            Class wordClass = Class.forName("net.ech.anagram.words.en_US.Words");
            InputStream wordIn = wordClass.getClassLoader().getResourceAsStream("words");
            if (wordIn == null)
            {
                throw new Exception ("Cannot open resource \"words\"");
            }

            WordList.loadWordsAndClose (wordIn, d);
        }

        if (doPrintStats)
        {
            d.printStats(output);
        }
        if (doDump)
        {
            d.dump(output);
        }

        Anagrammer anagrammer = new Anagrammer(dict);

        // Decide between interactive and non-interactive modes.
        if (inputWordList.size() > 0)
        {
            // Non-interactive mode.
            LetterBag input = new LetterBag(dict.getCharacterSet());
            for (String w : inputWordList)
            {
                LetterBag word = new LetterBag(dict.getCharacterSet());
                word.add(w);
                input.addWord(word);
            }
            anagrammer.findAnagrams (input, new ResultWriter (output));
        }
        else if (!doPrintStats && !doDump)
        {
            // Interactive mode.
            BufferedReader in =
                new BufferedReader (new InputStreamReader (System.in));
            for (;;)
            {
                System.out.print ("Enter a word or phrase: ");
                System.out.flush ();
                String line = in.readLine ();
                if (line == null || line.length () == 0)
                    break;
                anagrammer.findAnagrams (Anagrammer.digestInput(line, dict.getCharacterSet()), new ResultWriter (output));
            }
        }
    }

    private void usage ()
    {
        System.err.println ("usage: java " + Main.class + " -w <wordlist>");
        System.err.println ("or: java " + Main.class + " -w <wordlist> word...");
    }
}
