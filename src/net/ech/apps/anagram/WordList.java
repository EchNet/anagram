//
// WordList.java
//

package net.ech.apps.anagram;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.zip.*;
import net.ech.anagram.*;

public abstract class WordList
{
    /**
     * Load a set of words from a disk file.
     */
    public static void loadWords (String fileName, Dictionary dict)
        throws IOException, ParseException
    {
        // Check file for readability.
        File f = new File (fileName);
        if (!f.exists ())
            throw new IOException (fileName + ": not found");
        if (!f.canRead ())
            throw new IOException (fileName + ": cannot read");

        // Try to open as a zip file.
        ZipFile zf = null;
        try
        {
            zf = new ZipFile (f);
        }
        catch (IOException e)
        {
        }

        if (zf == null)
        {
            // Not a zip file.  Open as text file.
            loadWordsAndClose (new FileInputStream (f), dict);
        }
        else
        {
            // Zip file.  Open each entry as a text file.
            for (Enumeration entries = zf.entries ();
                 entries.hasMoreElements (); )
            {
                ZipEntry ze = (ZipEntry) entries.nextElement ();
                loadWordsAndClose (zf.getInputStream (ze), dict);
            }
        }
    }

    /**
     * Load a set of words from an input stream.
     */
    public static void loadWords (InputStream in, Dictionary dict)
        throws IOException, ParseException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        int count = 0;
        while ((line = reader.readLine()) != null)
        {
            if (count == 0)
            {
                if (line.length() > 0 && line.charAt(0) == '#')
                {
                    loadGroupedWords(reader, dict);
                    return;
                }
            }
            line = line.trim();
            if (line.length() > 0)
            {
                dict.addWord (line);
            }
        }
    }

    /**
     * @private
     */
    private static void loadGroupedWords(BufferedReader reader, Dictionary dict)
        throws ParseException, IOException
    {
        ICharacterSet charSet = dict.getCharacterSet();
        LetterBag bag = new LetterBag(charSet);
        List<String> list = new ArrayList<String>();
        String line;
        int count = 0;
        while ((line = reader.readLine()) != null)
        {
            String word = line.trim();
            LetterBag newBag = new LetterBag(charSet, word);
            if (!newBag.equals(bag))
            {
                if (list.size() > 0)
                {
                    dict.addFamily(bag, list);
                    list.clear();
                }
                bag = newBag;
            }
            list.add(word);
        }

        if (list.size() > 0)
        {
            dict.addFamily(bag, list);
        }
    }

    /**
     * Load a set of words from an input stream, then close the stream.
     */
    public static void loadWordsAndClose (InputStream in, Dictionary dict)
        throws IOException, ParseException
    {
        try
        {
            loadWords(in, dict);
        }
        finally
        {
            try
            {
                in.close ();
            }
            catch (IOException ignored)
            {
            }
        }
    }
}
