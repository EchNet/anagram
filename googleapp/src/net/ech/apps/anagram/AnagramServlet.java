//
// AnagramServlet.java
//

package net.ech.apps.anagram;

import java.io.*;
import javax.servlet.ServletException;    
import javax.servlet.http.*;
import net.ech.anagram.*;
import net.ech.anagram.csets.LowerCaseAscii;

@SuppressWarnings("serial")
/**
 * Servlet interface to anagrammer.
 */
public class AnagramServlet extends HttpServlet
{
    private ICharacterSet charSet = new LowerCaseAscii();

    public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }
    
    public void doGet(HttpServletRequest req, 
                      HttpServletResponse resp)
        throws IOException, ServletException
    {
        String fval = req.getParameter("f");
        String ival = req.getParameter("i");

        Form form = chooseForm(fval);
        validateInput(ival);

        form.process(ival, req, resp);
    }

    /**
     * Prevent rogue inputs from being processed.
     */
    private void validateInput(String ival)
        throws ServletException
    {
        if (ival == null)
        {
            throw new ServletException("i parameter missing");
        }
        if (ival.length() > 512)
        {
            throw new ServletException("max input length exceeded");
        }
    }

    /**
     * @private
     */
    private Form chooseForm(String fval)
        throws ServletException
    {
        if (fval == null)
        {
            return new HtmlForm();
        }
        else if (fval.equals("html"))
        {
            return new HtmlForm();
        }
        else if (fval.equals("ajax"))
        {
            return new AjaxForm();
        }
        else if (fval.equals("json"))
        {
            return new JsonForm();
        }
        else if (fval.equals("text"))
        {
            return new TextForm();
        }
        else
        {
            throw new ServletException("bad format");
        }
    }

    /**
     * @private
     */
    private void findAnagrams(String ival, IResultConsumer consumer)
        throws IOException
    {
    	try
        {
            LetterBag input = Anagrammer.digestInput(ival, charSet);
            Dictionary dict = new Dictionary(charSet);
            String locale = "en_US";
            Class wordsClass = Class.forName("net.ech.anagram.words." + locale + ".Words");
            InputStream dictIn = wordsClass.getClassLoader().getResourceAsStream(locale + ".txt");
            WordList.loadWordsAndClose(dictIn, dict);
            Anagrammer anagrammer = new Anagrammer(dict);
            anagrammer.findAnagrams(input, consumer);
        }
        catch (java.text.ParseException e)
        {
            // Bad character in input.  I guess that means no anagrams!
        }
        catch (IOException e)
        {
        	throw e;
        }
        catch (RuntimeException e)
        {
        	throw e;
        }
        catch (Exception e)
        {
        	throw new IOException (e);
        }
    }

    /**
     * @private
     */
    private abstract class Form
    {
        abstract public void process (String ival, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException;
    }

    /**
     * When HTML form is chosen, render the result text into a JSP.
     */
    private class HtmlForm
        extends Form
    {
        public void process (String ival, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException
        {
            AnagramCollector collector = new AnagramCollector(300);
            findAnagrams(ival, collector);
            req.setAttribute("anagram", collector);
            getServletContext().getRequestDispatcher("/resultpage.jsp").include(req, resp);
        }
    }

    /**
     * When HTML form is chosen, render the input text into a JSP.  The
     * rendered response page issues a secondary text form request.
     */
    private class AjaxForm
        extends Form
    {
        public void process (String ival, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException
        {
            req.setAttribute("input", ival);
            getServletContext().getRequestDispatcher("/resultpage.jsp").include(req, resp);
        }
    }

    /**
     * JSON not yet implemented.
     */
    private class JsonForm
        extends Form
    {
        public void process (String ival, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException
        {
            throw new ServletException("json not yet implemented");
        }
    }

    /**
     * When text form is chosen, deliver a plain text response.
     */
    private class TextForm
        extends Form
    {
        public void process (String ival, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException
        {
            AnagramCollector collector = new AnagramCollector(300);
            findAnagrams(ival, collector);
                                                                                            req.setAttribute("anagram", collector);
            getServletContext().getRequestDispatcher("/resulttext.jsp").include(req, resp);
        }
    }
}
