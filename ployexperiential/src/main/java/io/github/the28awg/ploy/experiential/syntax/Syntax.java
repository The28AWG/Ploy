package io.github.the28awg.ploy.experiential.syntax;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Syntax {
    public static final String LITERALS = "true,false,null";
    public static final String KEYWORDS = "abstract,continue,for,new,switch," +
            "assert,default,goto,package,synchronized," +
            "boolean,do,if,private,this," +
            "break,double,implements,protected,throw," +
            "byte,else,import,public,throws," +
            "case,enum,instanceof,return,transient," +
            "catch,extends,int,short,try," +
            "char,final,interface,static,void," +
            "class,finally,long,strictfp,volatile," +
            "const,float,native,super,while";
    /**
     * token style for a string literal
     */
    public static final String PR_STRING = "str";
    /**
     * token style for a keyword
     */
    public static final String PR_KEYWORD = "kwd";
    /**
     * token style for a comment
     */
    public static final String PR_COMMENT = "com";
    /**
     * token style for a type
     */
    public static final String PR_TYPE = "typ";
    /**
     * token style for a literal value.  e.g. 1, null, true.
     */
    public static final String PR_ANNOTATION = "ano";
    /**
     * token style for a literal value.  e.g. 1, null, true.
     */
    public static final String PR_LITERAL = "lit";
    /**
     * token style for a punctuation string.
     */
    public static final String PR_PUNCTUATION = "pun";
    /**
     * token style for a plain text.
     */
    public static final String PR_PLAIN = "pln";
    /**
     * token style for embedded source.
     */
    public static final String PR_SOURCE = "src";
    /**
     * Maps language-specific file extensions to handlers.
     */
    protected static final Map<String, Object> langHandlerRegistry = new HashMap<String, Object>();
    private static final Logger LOG = Logger.getLogger(Syntax.class.getName());
    private static final String REGEXP_PRECEDER_PATTERN = "(?:^^\\.?|[+-]|[!=]=?=?|\\#|%=?|&&?=?|\\(|\\*=?|[+\\-]=|->|\\/=?|::?|<<?=?|>>?>?=?|,|;|\\?|@|\\[|~|\\{|\\^\\^?=?|\\|\\|?=?|break|case|continue|delete|do|else|finally|instanceof|return|throw|try|typeof)\\s*";

    static {
        Map<String, Object> decorateSourceMap = new HashMap<>();
        decorateSourceMap.put("keywords", KEYWORDS);
        decorateSourceMap.put("literals", LITERALS);
        decorateSourceMap.put("cStyleComments", true);
        try {
            registerLangHandler(sourceDecorator(decorateSourceMap), Arrays.asList("java", "default-code"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    protected static Lexer sourceDecorator(Map<String, Object> options) throws Exception {
        List<List<Object>> shortcutStylePatterns = new ArrayList<List<Object>>();
        List<List<Object>> fallthroughStylePatterns = new ArrayList<List<Object>>();
        if (getVariableValueAsBoolean(options.get("tripleQuotedStrings"))) {
            // '''multi-line-string''', 'single-line-string', and double-quoted
            shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_STRING,
                    Pattern.compile("^(?:\\'\\'\\'(?:[^\\'\\\\]|\\\\[\\s\\S]|\\'{1,2}(?=[^\\']))*(?:\\'\\'\\'|$)|\\\"\\\"\\\"(?:[^\\\"\\\\]|\\\\[\\s\\S]|\\\"{1,2}(?=[^\\\"]))*(?:\\\"\\\"\\\"|$)|\\'(?:[^\\\\\\']|\\\\[\\s\\S])*(?:\\'|$)|\\\"(?:[^\\\\\\\"]|\\\\[\\s\\S])*(?:\\\"|$))"),
                    null,
                    "'\""}));
        } else if (getVariableValueAsBoolean(options.get("multiLineStrings"))) {
            // 'multi-line-string', "multi-line-string"
            shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_STRING,
                    Pattern.compile("^(?:\\'(?:[^\\\\\\']|\\\\[\\s\\S])*(?:\\'|$)|\\\"(?:[^\\\\\\\"]|\\\\[\\s\\S])*(?:\\\"|$)|\\`(?:[^\\\\\\`]|\\\\[\\s\\S])*(?:\\`|$))"),
                    null,
                    "'\"`"}));
        } else {
            // 'single-line-string', "single-line-string"
            shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_STRING,
                    Pattern.compile("^(?:\\'(?:[^\\\\\\'\r\n]|\\\\.)*(?:\\'|$)|\\\"(?:[^\\\\\\\"\r\n]|\\\\.)*(?:\\\"|$))"),
                    null,
                    "\"'"}));
        }
        if (getVariableValueAsBoolean(options.get("verbatimStrings"))) {
            // verbatim-string-literal production from the C# grammar.  See issue 93.
            fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_STRING,
                    Pattern.compile("^@\\\"(?:[^\\\"]|\\\"\\\")*(?:\\\"|$)"),
                    null}));
        }
        Object hc = options.get("hashComments");
        if (getVariableValueAsBoolean(hc)) {
            if (getVariableValueAsBoolean(options.get("cStyleComments"))) {
                if ((hc instanceof Integer) && (Integer) hc > 1) {  // multiline hash comments
                    shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_COMMENT,
                            Pattern.compile("^#(?:##(?:[^#]|#(?!##))*(?:###|$)|.*)"),
                            null,
                            "#"}));
                } else {
                    // Stop C preprocessor declarations at an unclosed open comment
                    shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_COMMENT,
                            Pattern.compile("^#(?:(?:define|e(?:l|nd)if|else|error|ifn?def|include|line|pragma|undef|warning)\\b|[^\r\n]*)"),
                            null,
                            "#"}));
                }
                // #include <stdio.h>
                fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_STRING,
                        Pattern.compile("^<(?:(?:(?:\\.\\.\\/)*|\\/?)(?:[\\w-]+(?:\\/[\\w-]+)+)?[\\w-]+\\.h(?:h|pp|\\+\\+)?|[a-z]\\w*)>"),
                        null}));
            } else {
                shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_COMMENT,
                        Pattern.compile("^#[^\r\n]*"),
                        null,
                        "#"}));
            }
        }
        if (getVariableValueAsBoolean(options.get("cStyleComments"))) {
            fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_COMMENT,
                    Pattern.compile("^\\/\\/[^\r\n]*"),
                    null}));

            fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_COMMENT,
                    Pattern.compile("^\\/\\*[\\s\\S]*?(?:\\*\\/|$)"),
                    null}));
        }
        Object regexLiterals = options.get("regexLiterals");
        if (getVariableValueAsBoolean(regexLiterals)) {
            /**
             * @const
             */
            // Javascript treat true as 1
            String regexExcls = getVariableValueAsInteger(regexLiterals) > 1
                    ? "" // Multiline regex literals
                    : "\n\r";
            /**
             * @const
             */
            String regexAny = !regexExcls.isEmpty() ? "." : "[\\S\\s]";
            /**
             * @const
             */
            String REGEX_LITERAL =
                    // A regular expression literal starts with a slash that is
                    // not followed by * or / so that it is not confused with
                    // comments.
                    "/(?=[^/*" + regexExcls + "])"
                            // and then contains any number of raw characters,
                            + "(?:[^/\\x5B\\x5C" + regexExcls + "]"
                            // escape sequences (\x5C),
                            + "|\\x5C" + regexAny
                            // or non-nesting character sets (\x5B\x5D);
                            + "|\\x5B(?:[^\\x5C\\x5D" + regexExcls + "]"
                            + "|\\x5C" + regexAny + ")*(?:\\x5D|$))+"
                            // finally closed by a /.
                            + "/";
            fallthroughStylePatterns.add(Arrays.asList(new Object[]{"lang-regex",
                    Pattern.compile("^" + REGEXP_PRECEDER_PATTERN + "(" + REGEX_LITERAL + ")")}));
        }

        Pattern types = (Pattern) options.get("types");
        if (getVariableValueAsBoolean(types)) {
            fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_TYPE, types}));
        }

        String keywords = (String) options.get("keywords");
        if (keywords != null) {
            keywords = keywords.replaceAll("^ | $", "");
            if (keywords.length() != 0) {
                fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_KEYWORD,
                        Pattern.compile("^(?:" + keywords.replaceAll("[\\s,]+", "|") + ")\\b"),
                        null}));
            }
        }

        String literals = (String) options.get("literals");
        if (literals != null) {
            literals = literals.replaceAll("^ | $", "");
            if (literals.length() != 0) {
                fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_LITERAL,
                        Pattern.compile("^(?:" + literals.replaceAll("[\\s,]+", "|") + ")\\b"),
                        null}));
                fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_LITERAL,
                        Pattern.compile("^(?:"
                                // A hex number
                                + "0x[a-f0-9]+"
                                // or an octal or decimal number,
                                + "|(?:\\d(?:_\\d+)*\\d*(?:\\.\\d*)?|\\.\\d\\+)"
                                // possibly in scientific notation
                                + "(?:e[+\\-]?\\d+)?"
                                + ')'
                                // with an optional modifier like UL for unsigned long
                                + "[a-z]*", Pattern.CASE_INSENSITIVE),
                        null,
                        "0123456789"}));
            }
        }

        shortcutStylePatterns.add(Arrays.asList(new Object[]{PR_PLAIN,
                Pattern.compile("^\\s+"),
                null,
                " \r\n\t" + Character.toString((char) 0xA0)
        }));

        // TODO(mikesamuel): recognize non-latin letters and numerals in idents
        fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_ANNOTATION,
                Pattern.compile("^@[a-z_$][a-z_$@0-9]*", Pattern.CASE_INSENSITIVE),
                null}));
        fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_TYPE,
                Pattern.compile("^(?:[@_]?[A-Z]+[a-z][A-Za-z_$@0-9]*|\\w+_t\\b)"),
                null}));
        fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_PLAIN,
                Pattern.compile("^[a-z_$][a-z_$@0-9]*", Pattern.CASE_INSENSITIVE),
                null}));
        // Don't treat escaped quotes in bash as starting strings.
        // See issue 144.
        fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_PLAIN,
                Pattern.compile("^\\\\[\\s\\S]?"),
                null}));

        String punctuation = "^.[^\\s\\w.$@'\"`/\\\\]*";
        if (getVariableValueAsBoolean(options.get("regexLiterals"))) {
            punctuation += "(?!\\s*/)";
        }
        fallthroughStylePatterns.add(Arrays.asList(new Object[]{PR_PUNCTUATION,
                Pattern.compile(punctuation),
                null}));

        return new Lexer(shortcutStylePatterns, fallthroughStylePatterns);
    }

    /**
     * Register a language handler for the given file extensions.
     *
     * @param handler        a function from source code to a list
     *                       of decorations.  Takes a single argument job which describes the
     *                       state of the computation.   The single parameter has the form
     *                       {@code {
     *                       sourceCode: {string} as plain text.
     *                       decorations: {Array.<number|string>} an array of style classes
     *                       preceded by the position at which they start in
     *                       job.sourceCode in order.
     *                       The language handler should assigned this field.
     *                       basePos: {int} the position of source in the larger source chunk.
     *                       All positions in the output decorations array are relative
     *                       to the larger source chunk.
     *                       } }
     * @param fileExtensions
     */
    protected static void registerLangHandler(Lexer handler, List<String> fileExtensions) throws Exception {
        for (int i = fileExtensions.size(); --i >= 0; ) {
            String ext = fileExtensions.get(i);
            if (langHandlerRegistry.get(ext) == null) {
                langHandlerRegistry.put(ext, handler);
            } else {
                throw new Exception("cannot override language handler " + ext);
            }
        }
    }

    /**
     * Register language handler. The clazz will not be instantiated
     *
     * @param clazz the class of the language
     * @throws Exception cannot instantiate the object using the class,
     *                   or language handler with specified extension exist already
     */
    public static void register(Class<? extends Lang> clazz) throws Exception {
        if (clazz == null) {
            throw new NullPointerException("argument 'clazz' cannot be null");
        }
        List<String> fileExtensions = getFileExtensionsFromClass(clazz);
        for (int i = fileExtensions.size(); --i >= 0; ) {
            String ext = fileExtensions.get(i);
            if (langHandlerRegistry.get(ext) == null) {
                langHandlerRegistry.put(ext, clazz);
            } else {
                throw new Exception("cannot override language handler " + ext);
            }
        }
    }

    protected static List<String> getFileExtensionsFromClass(Class<? extends Lang> clazz) throws Exception {
        Method getExtensionsMethod = clazz.getMethod("getFileExtensions", (Class<?>[]) null);
        return (List<String>) getExtensionsMethod.invoke(null, null);
    }

    /**
     * Get the parser for the extension specified.
     *
     * @param extension the file extension, if null, default parser will be returned
     * @param source    the source code
     * @return the parser
     */
    public static Lexer langHandlerForExtension(String extension, String source) {
        if (!(extension != null && langHandlerRegistry.get(extension) != null)) {
            // Treat it as markup if the first non whitespace character is a < and
            // the last non-whitespace character is a >.
            extension = test(Pattern.compile("^\\s*<"), source)
                    ? "default-markup"
                    : "default-code";
        }

        Object handler = langHandlerRegistry.get(extension);
        if (handler instanceof Lexer) {
            return (Lexer) handler;
        } else {
            Lexer _simpleLexer;
            try {
                Lang _lang = ((Class<Lang>) handler).newInstance();
                _simpleLexer = new Lexer(_lang.getShortcutStylePatterns(), _lang.getFallthroughStylePatterns());

                List<Lang> extendedLangs = _lang.getExtendedLangs();
                for (Lang _extendedLang : extendedLangs) {
                    register(_extendedLang.getClass());
                }

                List<String> fileExtensions = getFileExtensionsFromClass((Class<Lang>) handler);
                for (String _extension : fileExtensions) {
                    langHandlerRegistry.put(_extension, _simpleLexer);
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
                return null;
            }

            return _simpleLexer;
        }
    }

    /**
     * Apply the given language handler to sourceCode and add the resulting
     * decorations to out.
     *
     * @param basePos the index of sourceCode within the chunk of source
     *                whose decorations are already present on out.
     */
    protected static void appendDecorations(int basePos, String sourceCode, Lexer langHandler, List<Object> out) {
        if (sourceCode == null) {
            throw new NullPointerException("argument 'sourceCode' cannot be null");
        }
        Job job = new Job();
        job.setSourceCode(sourceCode);
        job.setBasePos(basePos);
        langHandler.decorate(job);
        out.addAll(job.getDecorations());
    }

    /**
     * Get all the matches for {@code string} compiled by {@code pattern}. If
     * {@code isGlobal} is true, the return results will only include the
     * group 0 matches. It is similar to string.match(regexp) in JavaScript.
     *
     * @param pattern  the regexp
     * @param string   the string
     * @param isGlobal similar to JavaScript /g flag
     * @return all matches
     */
    public static String[] match(Pattern pattern, String string, boolean isGlobal) {
        if (pattern == null) {
            throw new NullPointerException("argument 'pattern' cannot be null");
        }
        if (string == null) {
            throw new NullPointerException("argument 'string' cannot be null");
        }

        List<String> matchesList = new ArrayList<String>();

        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            matchesList.add(matcher.group(0));
            if (!isGlobal) {
                for (int i = 1, iEnd = matcher.groupCount(); i <= iEnd; i++) {
                    matchesList.add(matcher.group(i));
                }
            }
        }

        return matchesList.toArray(new String[matchesList.size()]);
    }

    /**
     * Join the {@code strings} into one string.
     *
     * @param strings the string list to join
     * @return the joined string
     */
    public static String join(List<String> strings) {
        if (strings == null) {
            throw new NullPointerException("argument 'strings' cannot be null");
        }
        return join(strings.toArray(new String[strings.size()]));
    }

    /**
     * Join the {@code strings} into one string with {@code delimiter} in
     * between.
     *
     * @param strings   the string list to join
     * @param delimiter the delimiter
     * @return the joined string
     */
    public static String join(List<String> strings, String delimiter) {
        if (strings == null) {
            throw new NullPointerException("argument 'strings' cannot be null");
        }
        return join(strings.toArray(new String[strings.size()]), delimiter);
    }

    /**
     * Join the {@code strings} into one string.
     *
     * @param strings the string list to join
     * @return the joined string
     */
    public static String join(String[] strings) {
        return join(strings, null);
    }

    /**
     * Join the {@code strings} into one string with {@code delimiter} in
     * between. It is similar to RegExpObject.test(string) in JavaScript.
     *
     * @param strings   the string list to join
     * @param delimiter the delimiter
     * @return the joined string
     */
    public static String join(String[] strings, String delimiter) {
        if (strings == null) {
            throw new NullPointerException("argument 'strings' cannot be null");
        }

        StringBuilder sb = new StringBuilder();

        if (strings.length != 0) {
            sb.append(strings[0]);
            for (int i = 1, iEnd = strings.length; i < iEnd; i++) {
                if (delimiter != null) {
                    sb.append(delimiter);
                }
                sb.append(strings[i]);
            }
        }

        return sb.toString();
    }

    /**
     * Test whether the {@code string} has at least one match by
     * {@code pattern}.
     *
     * @param pattern the regexp
     * @param string  the string to test
     * @return true if at least one match, false if no match
     */
    public static boolean test(Pattern pattern, String string) {
        if (pattern == null) {
            throw new NullPointerException("argument 'pattern' cannot be null");
        }
        if (string == null) {
            throw new NullPointerException("argument 'string' cannot be null");
        }
        return pattern.matcher(string).find();
    }

    /**
     * Remove identical adjacent tags from {@code decorations}.
     *
     * @param decorations see {@link Job#decorations}
     * @param source      the source code
     * @return the {@code decorations} after treatment
     * @throws IllegalArgumentException the size of {@code decoration} is not
     *                                  a multiple of 2
     */
    public static List<Object> removeDuplicates(List<Object> decorations, String source) {
        if (decorations == null) {
            throw new NullPointerException("argument 'decorations' cannot be null");
        }
        if (source == null) {
            throw new NullPointerException("argument 'source' cannot be null");
        }
        if ((decorations.size() & 0x1) != 0) {
            throw new IllegalArgumentException("the size of argument 'decorations' should be a multiple of 2");
        }

        List<Object> returnList = new ArrayList<Object>();

        // use TreeMap to remove entrys with same pos
        Map<Integer, Object> orderedMap = new TreeMap<>();
        for (int i = 0, iEnd = decorations.size(); i < iEnd; i += 2) {
            orderedMap.put((Integer) decorations.get(i), decorations.get(i + 1));
        }

        // remove adjacent style
        String previousStyle = null;
        for (Integer pos : orderedMap.keySet()) {
            String style = (String) orderedMap.get(pos);
            if (previousStyle != null && previousStyle.equals(style)) {
                continue;
            }
            returnList.add(pos);
            returnList.add(style);
            previousStyle = style;
        }

        // remove last zero length tag
        int returnListSize = returnList.size();
        if (returnListSize >= 4 && returnList.get(returnListSize - 2).equals(source.length())) {
            returnList.remove(returnListSize - 2);
            returnList.remove(returnListSize - 2);
        }

        return returnList;
    }

    /**
     * Treat a variable as an boolean in JavaScript style. Note this function can
     * only handle string, integer and boolean currently. All other data type, if
     * null, return false, not null return true.
     *
     * @param var the variable to get value from
     * @return the boolean value
     */
    public static Boolean getVariableValueAsBoolean(Object var) {
        Boolean returnResult = null;

        if (var == null) {
            returnResult = false;
        } else if (var instanceof String) {
            returnResult = !((String) var).isEmpty();
        } else if (var instanceof Integer) {
            returnResult = ((Integer) var) != 0;
        } else if (var instanceof Boolean) {
            returnResult = (Boolean) var;
        } else {
            returnResult = true;
        }

        return returnResult;
    }

    /**
     * Treat a variable as an integer in JavaScript style. Note this function can
     * only handle integer and boolean currently.
     *
     * @param var the variable to get value from
     * @return the integer value
     * @throws IllegalArgumentException the data type of {@code var} is neither
     *                                  integer nor boolean.
     */
    public static Integer getVariableValueAsInteger(Object var) {
        if (var == null) {
            throw new NullPointerException("argument 'var' cannot be null");
        }

        Integer returnResult = -1;

        if (var instanceof Integer) {
            returnResult = (Integer) var;
        } else if (var instanceof Boolean) {
            // Javascript treat true as 1
            returnResult = (Boolean) var ? 1 : 0;
        } else {
            throw new IllegalArgumentException("'var' is neither integer nor boolean");
        }

        return returnResult;
    }

    public static class Lexer {

        protected List<List<Object>> fallthroughStylePatterns;
        protected Map<Character, List<Object>> shortcuts = new HashMap<Character, List<Object>>();
        protected Pattern tokenizer;
        protected int nPatterns;

        /**
         * Given triples of [style, pattern, context] returns a lexing function,
         * The lexing function interprets the patterns to find token boundaries and
         * returns a decoration list of the form
         * [index_0, style_0, index_1, style_1, ..., index_n, style_n]
         * where index_n is an index into the sourceCode, and style_n is a style
         * constant like PR_PLAIN.  index_n-1 <= index_n, and style_n-1 applies to
         * all characters in sourceCode[index_n-1:index_n].
         * <p>
         * The stylePatterns is a list whose elements have the form
         * [style : string, pattern : RegExp, DEPRECATED, shortcut : string].
         * <p>
         * Style is a style constant like PR_PLAIN, or can be a string of the
         * form 'lang-FOO', where FOO is a language extension describing the
         * language of the portion of the token in $1 after pattern executes.
         * E.g., if style is 'lang-lisp', and group 1 contains the text
         * '(hello (world))', then that portion of the token will be passed to the
         * registered lisp handler for formatting.
         * The text before and after group 1 will be restyled using this decorator
         * so decorators should take care that this doesn't result in infinite
         * recursion.  For example, the HTML lexer rule for SCRIPT elements looks
         * something like ['lang-js', /<[s]cript>(.+?)<\/script>/].  This may match
         * '<script>foo()<\/script>', which would cause the current decorator to
         * be called with '<script>' which would not match the same rule since
         * group 1 must not be empty, so it would be instead styled as PR_TAG by
         * the generic tag rule.  The handler registered for the 'js' extension would
         * then be called with 'foo()', and finally, the current decorator would
         * be called with '<\/script>' which would not match the original rule and
         * so the generic tag rule would identify it as a tag.
         * <p>
         * Pattern must only match prefixes, and if it matches a prefix, then that
         * match is considered a token with the same style.
         * <p>
         * Context is applied to the last non-whitespace, non-comment token
         * recognized.
         * <p>
         * Shortcut is an optional string of characters, any of which, if the first
         * character, gurantee that this pattern and only this pattern matches.
         *
         * @param shortcutStylePatterns    patterns that always start with
         *                                 a known character.  Must have a shortcut string.
         * @param fallthroughStylePatterns patterns that will be tried in
         *                                 order if the shortcut ones fail.  May have shortcuts.
         */
        protected Lexer(List<List<Object>> shortcutStylePatterns, List<List<Object>> fallthroughStylePatterns) throws Exception {
            this.fallthroughStylePatterns = fallthroughStylePatterns;

            List<List<Object>> allPatterns = new ArrayList<>(shortcutStylePatterns);
            allPatterns.addAll(fallthroughStylePatterns);
            List<Pattern> allRegexs = new ArrayList<Pattern>();
            Map<String, Object> regexKeys = new HashMap<String, Object>();
            for (int i = 0, n = allPatterns.size(); i < n; ++i) {
                List<Object> patternParts = allPatterns.get(i);
                String shortcutChars = patternParts.size() > 3 ? (String) patternParts.get(3) : null;
                if (shortcutChars != null) {
                    for (int c = shortcutChars.length(); --c >= 0; ) {
                        shortcuts.put(shortcutChars.charAt(c), patternParts);
                    }
                }
                Pattern regex = (Pattern) patternParts.get(1);
                String k = regex.pattern();
                if (regexKeys.get(k) == null) {
                    allRegexs.add(regex);
                    regexKeys.put(k, new Object());
                }
            }
            allRegexs.add(Pattern.compile("[\0-\\uffff]"));
            tokenizer = new CombinePrefixPattern().combinePrefixPattern(allRegexs);

            nPatterns = fallthroughStylePatterns.size();
        }

        /**
         * Lexes job.sourceCode and produces an output array job.decorations of
         * style classes preceded by the position at which they start in
         * job.sourceCode in order.
         *
         * @param job an object like <pre>{
         *                                     sourceCode: {string} sourceText plain text,
         *                                     basePos: {int} position of job.sourceCode in the larger chunk of
         *                                         sourceCode.
         *                                  }</pre>
         */
        public void decorate(Job job) {
            String sourceCode = job.getSourceCode();
            int basePos = job.getBasePos();
            /** Even entries are positions in source in ascending order.  Odd enties
             * are style markers (e.g., PR_COMMENT) that run from that position until
             * the end.
             * @type {Array.<number|string>}
             */
            List<Object> decorations = new ArrayList<Object>(Arrays.asList(new Object[]{basePos, PR_PLAIN}));
            int pos = 0;  // index into sourceCode
            String[] tokens = match(tokenizer, sourceCode, true);
            Map<String, String> styleCache = new HashMap<String, String>();

            for (int ti = 0, nTokens = tokens.length; ti < nTokens; ++ti) {
                String token = tokens[ti];
                String style = styleCache.get(token);
                String[] match = null;

                boolean isEmbedded;
                if (style != null) {
                    isEmbedded = false;
                } else {
                    List<Object> patternParts = shortcuts.get(token.charAt(0));
                    if (patternParts != null) {
                        match = match((Pattern) patternParts.get(1), token, false);
                        style = (String) patternParts.get(0);
                    } else {
                        for (int i = 0; i < nPatterns; ++i) {
                            patternParts = fallthroughStylePatterns.get(i);
                            match = match((Pattern) patternParts.get(1), token, false);
                            if (match.length != 0) {
                                style = (String) patternParts.get(0);
                                break;
                            }
                        }

                        if (match.length == 0) {  // make sure that we make progress
                            style = PR_PLAIN;
                        }
                    }

                    isEmbedded = style != null && style.length() >= 5 && style.startsWith("lang-");
                    if (isEmbedded && !(match.length > 1 && match[1] != null)) {
                        isEmbedded = false;
                        style = PR_SOURCE;
                    }

                    if (!isEmbedded) {
                        styleCache.put(token, style);
                    }
                }

                int tokenStart = pos;
                pos += token.length();

                if (!isEmbedded) {
                    decorations.add(basePos + tokenStart);
                    decorations.add(style);
                } else {  // Treat group 1 as an embedded block of source code.
                    String embeddedSource = match[1];
                    int embeddedSourceStart = token.indexOf(embeddedSource);
                    int embeddedSourceEnd = embeddedSourceStart + embeddedSource.length();
                    if (match.length > 2 && match[2] != null) {
                        // If embeddedSource can be blank, then it would match at the
                        // beginning which would cause us to infinitely recurse on the
                        // entire token, so we catch the right context in match[2].
                        embeddedSourceEnd = token.length() - match[2].length();
                        embeddedSourceStart = embeddedSourceEnd - embeddedSource.length();
                    }
                    String lang = style.substring(5);
                    // Decorate the left of the embedded source
                    appendDecorations(basePos + tokenStart,
                            token.substring(0, embeddedSourceStart),
                            this, decorations);
                    // Decorate the embedded source
                    appendDecorations(basePos + tokenStart + embeddedSourceStart,
                            embeddedSource,
                            langHandlerForExtension(lang, embeddedSource),
                            decorations);
                    // Decorate the right of the embedded section
                    appendDecorations(basePos + tokenStart + embeddedSourceEnd,
                            token.substring(embeddedSourceEnd),
                            this, decorations);
                }
            }

            job.setDecorations(removeDuplicates(decorations, job.getSourceCode()));
        }
    }

    public static class CombinePrefixPattern {

        protected static final Map<Character, Integer> escapeCharToCodeUnit = new HashMap<Character, Integer>();

        static {
            escapeCharToCodeUnit.put('b', 8);
            escapeCharToCodeUnit.put('t', 9);
            escapeCharToCodeUnit.put('n', 0xa);
            escapeCharToCodeUnit.put('v', 0xb);
            escapeCharToCodeUnit.put('f', 0xc);
            escapeCharToCodeUnit.put('r', 0xf);
        }

        protected int capturedGroupIndex = 0;
        protected boolean needToFoldCase = false;

        public CombinePrefixPattern() {
        }

        protected static int decodeEscape(String charsetPart) {
            Integer cc0 = charsetPart.codePointAt(0);
            if (cc0 != 92 /* \\ */) {
                return cc0;
            }
            char c1 = charsetPart.charAt(1);
            cc0 = escapeCharToCodeUnit.get(c1);
            if (cc0 != null) {
                return cc0;
            } else if ('0' <= c1 && c1 <= '7') {
                return Integer.parseInt(charsetPart.substring(1), 8);
            } else if (c1 == 'u' || c1 == 'x') {
                return Integer.parseInt(charsetPart.substring(2), 16);
            } else {
                return charsetPart.codePointAt(1);
            }
        }

        protected static String encodeEscape(int charCode) {
            if (charCode < 0x20) {
                return (charCode < 0x10 ? "\\x0" : "\\x") + Integer.toString(charCode, 16);
            }

            String ch = new String(Character.toChars(charCode));
            return (charCode == '\\' || charCode == '-' || charCode == ']' || charCode == '^')
                    ? "\\" + ch : ch;
        }

        protected static String caseFoldCharset(String charSet) {
            String[] charsetParts = match(Pattern.compile("\\\\u[0-9A-Fa-f]{4}"
                    + "|\\\\x[0-9A-Fa-f]{2}"
                    + "|\\\\[0-3][0-7]{0,2}"
                    + "|\\\\[0-7]{1,2}"
                    + "|\\\\[\\s\\S]"
                    + "|-"
                    + "|[^-\\\\]"), charSet.substring(1, charSet.length() - 1), true);
            List<List<Integer>> ranges = new ArrayList<List<Integer>>();
            boolean inverse = charsetParts[0] != null && charsetParts[0].equals("^");

            List<String> out = new ArrayList<String>(Arrays.asList(new String[]{"["}));
            if (inverse) {
                out.add("^");
            }

            for (int i = inverse ? 1 : 0, n = charsetParts.length; i < n; ++i) {
                String p = charsetParts[i];
                if (test(Pattern.compile("\\\\[bdsw]", Pattern.CASE_INSENSITIVE), p)) {  // Don't muck with named groups.
                    out.add(p);
                } else {
                    int start = decodeEscape(p);
                    int end;
                    if (i + 2 < n && "-".equals(charsetParts[i + 1])) {
                        end = decodeEscape(charsetParts[i + 2]);
                        i += 2;
                    } else {
                        end = start;
                    }
                    ranges.add(Arrays.asList(start, end));
                    // If the range might intersect letters, then expand it.
                    // This case handling is too simplistic.
                    // It does not deal with non-latin case folding.
                    // It works for latin source code identifiers though.
                    if (!(end < 65 || start > 122)) {
                        if (!(end < 65 || start > 90)) {
                            ranges.add(Arrays.asList(Math.max(65, start) | 32, Math.min(end, 90) | 32));
                        }
                        if (!(end < 97 || start > 122)) {
                            ranges.add(Arrays.asList(Math.max(97, start) & ~32, Math.min(end, 122) & ~32));
                        }
                    }
                }
            }

            // [[1, 10], [3, 4], [8, 12], [14, 14], [16, 16], [17, 17]]
            // -> [[1, 12], [14, 14], [16, 17]]
            Collections.sort(ranges, new Comparator<List<Integer>>() {

                @Override
                public int compare(List<Integer> a, List<Integer> b) {
                    return a.get(0) != b.get(0) ? (a.get(0) - b.get(0)) : (b.get(1) - a.get(1));
                }
            });
            List<List<Integer>> consolidatedRanges = new ArrayList<List<Integer>>();
//        List<Integer> lastRange = Arrays.asList(new Integer[]{0, 0});
            List<Integer> lastRange = new ArrayList<Integer>(Arrays.asList(new Integer[]{0, 0}));
            for (int i = 0; i < ranges.size(); ++i) {
                List<Integer> range = ranges.get(i);
                if (lastRange.get(1) != null && range.get(0) <= lastRange.get(1) + 1) {
                    lastRange.set(1, Math.max(lastRange.get(1), range.get(1)));
                } else {
                    // reference of lastRange is added
                    consolidatedRanges.add(lastRange = range);
                }
            }

            for (int i = 0; i < consolidatedRanges.size(); ++i) {
                List<Integer> range = consolidatedRanges.get(i);
                out.add(encodeEscape(range.get(0)));
                if (range.get(1) > range.get(0)) {
                    if (range.get(1) + 1 > range.get(0)) {
                        out.add("-");
                    }
                    out.add(encodeEscape(range.get(1)));
                }
            }
            out.add("]");

            return join(out);
        }

        /**
         * Given a group of {@link java.util.regex.Pattern}s, returns a {@code RegExp} that globally
         * matches the union of the sets of strings matched by the input RegExp.
         * Since it matches globally, if the input strings have a start-of-input
         * anchor (/^.../), it is ignored for the purposes of unioning.
         *
         * @param regexs non multiline, non-global regexs.
         * @return Pattern a global regex.
         */
        public Pattern combinePrefixPattern(List<Pattern> regexs) throws Exception {
            boolean ignoreCase = false;

            for (int i = 0, n = regexs.size(); i < n; ++i) {
                Pattern regex = regexs.get(i);
                if ((regex.flags() & Pattern.CASE_INSENSITIVE) != 0) {
                    ignoreCase = true;
                } else if (test(Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE), regex.pattern().replaceAll("\\\\[Uu][0-9A-Fa-f]{4}|\\\\[Xx][0-9A-Fa-f]{2}|\\\\[^UuXx]", ""))) {
                    needToFoldCase = true;
                    ignoreCase = false;
                    break;
                }
            }

            List<String> rewritten = new ArrayList<String>();
            for (int i = 0, n = regexs.size(); i < n; ++i) {
                Pattern regex = regexs.get(i);
                if ((regex.flags() & Pattern.MULTILINE) != 0) {
                    throw new Exception(regex.pattern());
                }
                rewritten.add("(?:" + allowAnywhereFoldCaseAndRenumberGroups(regex) + ")");
            }

            return ignoreCase ? Pattern.compile(join(rewritten, "|"), Pattern.CASE_INSENSITIVE) : Pattern.compile(join(rewritten, "|"));
        }

        protected String allowAnywhereFoldCaseAndRenumberGroups(Pattern regex) {
            // Split into character sets, escape sequences, punctuation strings
            // like ('(', '(?:', ')', '^'), and runs of characters that do not
            // include any of the above.
            String[] parts = match(Pattern.compile("(?:"
                    + "\\[(?:[^\\x5C\\x5D]|\\\\[\\s\\S])*\\]" // a character set
                    + "|\\\\u[A-Fa-f0-9]{4}" // a unicode escape
                    + "|\\\\x[A-Fa-f0-9]{2}" // a hex escape
                    + "|\\\\[0-9]+" // a back-reference or octal escape
                    + "|\\\\[^ux0-9]" // other escape sequence
                    + "|\\(\\?[:!=]" // start of a non-capturing group
                    + "|[\\(\\)\\^]" // start/end of a group, or line start
                    + "|[^\\x5B\\x5C\\(\\)\\^]+" // run of other characters
                    + ")"), regex.pattern(), true);
            int n = parts.length;

            // Maps captured group numbers to the number they will occupy in
            // the output or to -1 if that has not been determined, or to
            // undefined if they need not be capturing in the output.
            Map<Integer, Integer> capturedGroups = new HashMap<Integer, Integer>();

            // Walk over and identify back references to build the capturedGroups
            // mapping.
            for (int i = 0, groupIndex = 0; i < n; ++i) {
                String p = parts[i];
                if (p.equals("(")) {
                    // groups are 1-indexed, so max group index is count of '('
                    ++groupIndex;
                } else if ('\\' == p.charAt(0)) {
                    try {
                        int decimalValue = Math.abs(Integer.parseInt(p.substring(1)));
                        if (decimalValue <= groupIndex) {
                            capturedGroups.put(decimalValue, -1);
                        } else {
                            // Replace with an unambiguous escape sequence so that
                            // an octal escape sequence does not turn into a backreference
                            // to a capturing group from an earlier regex.
                            parts[i] = encodeEscape(decimalValue);
                        }
                    } catch (NumberFormatException ex) {
                    }
                }
            }

            // Renumber groups and reduce capturing groups to non-capturing groups
            // where possible.
            for (int i : capturedGroups.keySet()) {
                if (-1 == capturedGroups.get(i)) {
                    capturedGroups.put(i, ++capturedGroupIndex);
                }
            }
            for (int i = 0, groupIndex = 0; i < n; ++i) {
                String p = parts[i];
                if (p.equals("(")) {
                    ++groupIndex;
                    if (capturedGroups.get(groupIndex) == null) {
                        parts[i] = "(?:";
                    }
                } else if ('\\' == p.charAt(0)) {
                    try {
                        int decimalValue = Math.abs(Integer.parseInt(p.substring(1)));
                        if (decimalValue <= groupIndex) {
                            parts[i] = "\\" + capturedGroups.get(decimalValue);
                        }
                    } catch (NumberFormatException ex) {
                    }
                }
            }

            // Remove any prefix anchors so that the output will match anywhere.
            // ^^ really does mean an anchored match though.
            for (int i = 0; i < n; ++i) {
                if ("^".equals(parts[i]) && !"^".equals(parts[i + 1])) {
                    parts[i] = "";
                }
            }

            // Expand letters to groups to handle mixing of case-sensitive and
            // case-insensitive patterns if necessary.
            if ((regex.flags() & Pattern.CASE_INSENSITIVE) != 0 && needToFoldCase) {
                for (int i = 0; i < n; ++i) {
                    String p = parts[i];
                    char ch0 = p.length() > 0 ? p.charAt(0) : 0;
                    if (p.length() >= 2 && ch0 == '[') {
                        parts[i] = caseFoldCharset(p);
                    } else if (ch0 != '\\') {
                        // TODO: handle letters in numeric escapes.
                        StringBuffer sb = new StringBuffer();
                        Matcher _matcher = Pattern.compile("[a-zA-Z]").matcher(p);
                        while (_matcher.find()) {
                            int cc = _matcher.group(0).codePointAt(0);
                            _matcher.appendReplacement(sb, "");
                            sb.append("[").append(Character.toString((char) (cc & ~32))).append(Character.toString((char) (cc | 32))).append("]");
                        }
                        _matcher.appendTail(sb);
                        parts[i] = sb.toString();
                    }
                }
            }

            return join(parts);
        }
    }
}