anagrammara: an anagram finder Google app.


KNOWN BUGS 

    No indication of empty results.

    Poor error handling for bad characters (and other failures)
    HTML: There are no instructions for using the site.

    Input length is limited to 50 characters.
    Web: The home page is not crawled.
    Collapse white space.


WISH LIST

    HTML: nicer formatting of results page.
    Faster dictionary load.
        - Performance benchmark
        - Prebuilt dictionary with index, stored as BLOB.
        - memcache.
    Dictionary administration (offline tools & upload?).
    User-defined dictionaries.
    Rich client: let the user visualize and recombine multigrams.
    Typeahead


1.1

    HTML:

     x  Divide it up between home page and results page.  Like Google.
        Results page has all the options of home page, but tighter formatting.
     x  Work if Javascript disabled.
     x  There are two forms of results page: dynamic and static.
            - Static renders completely on server.
            - Dynamic renders immediately, then populates the list from
              server request.
            - Dynamic depends on Javascript and is reached only by JS.
        Results page shows the current search terms immediately.
     x  Limit number of basic results to 300 (?)
     x  Rename to "Anagrammara"
        Add an "about" page.
            - Look into free wiki.
        Link from ech.net
        Add crawler keywords

    Dictionary:

     x  Work on dictionary loading time.
     x  Get a better list of words

    Bugs:
     x  No indication of empty results.

     x  Poor error handling for bad characters (and other failures)
        HTML: There are no instructions for using the site.

     x  Input length is limited to 50 characters.
        Web: The home page is not crawled.
     x  Collapse white space.

1.2
