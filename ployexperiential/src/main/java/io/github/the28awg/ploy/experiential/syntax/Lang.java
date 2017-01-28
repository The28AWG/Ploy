package io.github.the28awg.ploy.experiential.syntax;

import java.util.ArrayList;
import java.util.List;

public abstract class Lang {

    /**
     * Similar to those in JavaScript prettify.js.
     */
    protected List<List<Object>> shortcutStylePatterns;
    /**
     * Similar to those in JavaScript prettify.js.
     */
    protected List<List<Object>> fallthroughStylePatterns;
    /**
     */
    protected List<Lang> extendedLangs;

    /**
     * Constructor.
     */
    public Lang() {
        shortcutStylePatterns = new ArrayList<List<Object>>();
        fallthroughStylePatterns = new ArrayList<List<Object>>();
        extendedLangs = new ArrayList<Lang>();
    }

    /**
     * This method should be overridden by the child class.
     * This provide the file extensions list to help the parser to determine which
     * {@link Lang} to use. See JavaScript prettify.js.
     *
     * @return the list of file extensions
     */
    public static List<String> getFileExtensions() {
        return new ArrayList<String>();
    }

    public List<List<Object>> getShortcutStylePatterns() {
        List<List<Object>> returnList = new ArrayList<List<Object>>();
        for (List<Object> shortcutStylePattern : shortcutStylePatterns) {
            returnList.add(new ArrayList<Object>(shortcutStylePattern));
        }
        return returnList;
    }

    public void setShortcutStylePatterns(List<List<Object>> shortcutStylePatterns) {
        if (shortcutStylePatterns == null) {
            this.shortcutStylePatterns = new ArrayList<List<Object>>();
            return;
        }
        List<List<Object>> cloneList = new ArrayList<List<Object>>();
        for (List<Object> shortcutStylePattern : shortcutStylePatterns) {
            cloneList.add(new ArrayList<Object>(shortcutStylePattern));
        }
        this.shortcutStylePatterns = cloneList;
    }

    public List<List<Object>> getFallthroughStylePatterns() {
        List<List<Object>> returnList = new ArrayList<List<Object>>();
        for (List<Object> fallthroughStylePattern : fallthroughStylePatterns) {
            returnList.add(new ArrayList<Object>(fallthroughStylePattern));
        }
        return returnList;
    }

    public void setFallthroughStylePatterns(List<List<Object>> fallthroughStylePatterns) {
        if (fallthroughStylePatterns == null) {
            this.fallthroughStylePatterns = new ArrayList<List<Object>>();
            return;
        }
        List<List<Object>> cloneList = new ArrayList<List<Object>>();
        for (List<Object> fallthroughStylePattern : fallthroughStylePatterns) {
            cloneList.add(new ArrayList<Object>(fallthroughStylePattern));
        }
        this.fallthroughStylePatterns = cloneList;
    }

    /**
     * Get the extended languages list.
     *
     * @return the list
     */
    public List<Lang> getExtendedLangs() {
        return new ArrayList<Lang>(extendedLangs);
    }

    /**
     * Set extended languages. Because we cannot register multiple languages
     * within one {@link Lang}, so it is used as an solution.
     *
     * @param extendedLangs the list of {@link Lang}s
     */
    public void setExtendedLangs(List<Lang> extendedLangs) {
        this.extendedLangs = new ArrayList<Lang>(extendedLangs);
    }
}

