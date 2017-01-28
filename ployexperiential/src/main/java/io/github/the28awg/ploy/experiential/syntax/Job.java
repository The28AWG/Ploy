package io.github.the28awg.ploy.experiential.syntax;

import java.util.ArrayList;
import java.util.List;

public class Job {

    /**
     * The starting point of the source code.
     */
    protected int basePos;
    /**
     * The source code.
     */
    protected String sourceCode;
    /**
     * The parsed results. n<sup>th</sup> items are starting position position,
     * n+1<sup>th</sup> items are the three-letter style keyword, where n start
     * from 0.
     */
    protected List<Object> decorations;

    /**
     * Constructor.
     */
    public Job() {
        this(0, "");
    }

    /**
     * Constructor.
     *
     * @param basePos    the starting point of the source code
     * @param sourceCode the source code
     */
    public Job(int basePos, String sourceCode) {
        if (sourceCode == null) {
            throw new NullPointerException("argument 'sourceCode' cannot be null");
        }
        this.basePos = basePos;
        this.sourceCode = sourceCode;
        decorations = new ArrayList<>();
    }

    /**
     * Set the starting point of the source code.
     *
     * @return the position
     */
    public int getBasePos() {
        return basePos;
    }

    /**
     * Set the starting point of the source code.
     *
     * @param basePos the position
     */
    public void setBasePos(int basePos) {
        this.basePos = basePos;
    }

    /**
     * Get the source code.
     *
     * @return the source code
     */
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * Set the source code.
     *
     * @param sourceCode the source code
     */
    public void setSourceCode(String sourceCode) {
        if (sourceCode == null) {
            throw new NullPointerException("argument 'sourceCode' cannot be null");
        }
        this.sourceCode = sourceCode;
    }

    /**
     * Get the parsed results. see {@link #decorations}.
     *
     * @return the parsed results
     */
    public List<Object> getDecorations() {
        return new ArrayList<>(decorations);
    }

    /**
     * Set the parsed results. see {@link #decorations}.
     *
     * @param decorations the parsed results
     */
    public void setDecorations(List<Object> decorations) {
        if (decorations == null) {
            this.decorations = new ArrayList<>();
            return;
        }
        this.decorations = new ArrayList<>(decorations);
    }
}
