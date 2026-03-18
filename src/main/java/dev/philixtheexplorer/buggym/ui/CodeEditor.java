package dev.philixtheexplorer.buggym.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.IndexRange;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A syntax-highlighted code editor for Java code using RichTextFX.
 */
public class CodeEditor extends StackPane {

    private static final String INDENT = "    ";

    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while",
            "var", "record", "sealed", "permits", "non-sealed", "yield"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "[()]";
    private static final String BRACE_PATTERN = "[{}]";
    private static final String BRACKET_PATTERN = "[\\[\\]]";
    private static final String SEMICOLON_PATTERN = ";";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String CHAR_PATTERN = "'([^'\\\\]|\\\\.)*'";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?[fFdDlL]?\\b";
    private static final String ANNOTATION_PATTERN = "@\\w+";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<CHAR>" + CHAR_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<ANNOTATION>" + ANNOTATION_PATTERN + ")");

    private final CodeArea codeArea;
    private int fontSize = 14;
    private boolean autoIndentEnabled = true;
    private boolean autoBracketPairingEnabled = true;

    public CodeEditor() {
        this.codeArea = new CodeArea();

        // Set up line numbers
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        // Set up syntax highlighting with debounce
        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(100))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));

        // Handle tab key for indentation and Enter key for auto indent
        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                if (event.isShiftDown()) {
                    // Shift+Tab: Remove indentation
                    removeIndentation();
                } else {
                    // Tab: Add indentation
                    codeArea.insertText(codeArea.getCaretPosition(), INDENT);
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER && autoIndentEnabled) {
                handleAutoIndentOnEnter();
                event.consume();
            }
        });

        codeArea.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!autoBracketPairingEnabled || event.isControlDown() || event.isAltDown() || event.isMetaDown()) {
                return;
            }

            String typed = event.getCharacter();
            if (typed == null || typed.isEmpty()) {
                return;
            }

            char ch = typed.charAt(0);
            if (isOpeningBracket(ch)) {
                handleOpeningBracket(ch);
                event.consume();
            } else if (isClosingBracket(ch)) {
                if (skipExistingClosingBracket(ch)) {
                    event.consume();
                }
            }
        });

        // Apply styles
        this.getStyleClass().add("code-editor");
        codeArea.getStyleClass().add("code-area");
        this.getStylesheets().add(getClass().getResource("/styles/code-editor.css").toExternalForm());
        applyFontSize();

        getChildren().add(codeArea);
    }

    private void changeFontSize(int delta) {
        int newSize = fontSize + delta;
        if (newSize >= 10 && newSize <= 40) {
            fontSize = newSize;
            applyFontSize();
        }
    }

    public void increaseFontSize() {
        changeFontSize(2);
    }

    public void decreaseFontSize() {
        changeFontSize(-2);
    }

    public void resetFontSize() {
        fontSize = 14;
        applyFontSize();
    }

    private void applyFontSize() {
        codeArea.setStyle("-fx-font-size: " + fontSize + "px;");
    }

    private void removeIndentation() {
        int caretPos = codeArea.getCaretPosition();
        int lineStart = codeArea.getText().lastIndexOf('\n', caretPos - 1) + 1;
        String lineText = codeArea.getText(lineStart, caretPos);

        if (lineText.startsWith("    ")) {
            codeArea.deleteText(lineStart, lineStart + 4);
        } else if (lineText.startsWith("\t")) {
            codeArea.deleteText(lineStart, lineStart + 1);
        }
    }

    private void handleAutoIndentOnEnter() {
        int caretPos = codeArea.getCaretPosition();
        String text = codeArea.getText();

        int lineStart = text.lastIndexOf('\n', Math.max(0, caretPos - 1)) + 1;
        String lineBeforeCaret = text.substring(lineStart, caretPos);
        String baseIndent = leadingWhitespace(lineBeforeCaret);
        boolean opensBlock = lineBeforeCaret.stripTrailing().endsWith("{");
        char nextVisibleChar = nextNonWhitespaceChar(text, caretPos);

        if (opensBlock && nextVisibleChar == '}') {
            String insertion = "\n" + baseIndent + INDENT + "\n" + baseIndent;
            codeArea.insertText(caretPos, insertion);
            codeArea.moveTo(caretPos + 1 + baseIndent.length() + INDENT.length());
            return;
        }

        String indentToInsert = baseIndent;
        if (opensBlock) {
            indentToInsert += INDENT;
        }

        codeArea.insertText(caretPos, "\n" + indentToInsert);
        codeArea.moveTo(caretPos + 1 + indentToInsert.length());
    }

    private void handleOpeningBracket(char opening) {
        char closing = matchingBracket(opening);
        IndexRange selection = codeArea.getSelection();

        if (selection.getLength() > 0) {
            String selectedText = codeArea.getSelectedText();
            codeArea.replaceText(selection.getStart(), selection.getEnd(), "" + opening + selectedText + closing);
            codeArea.selectRange(selection.getStart() + 1, selection.getEnd() + 1);
            return;
        }

        int caretPos = codeArea.getCaretPosition();
        codeArea.insertText(caretPos, "" + opening + closing);
        codeArea.moveTo(caretPos + 1);
    }

    private boolean skipExistingClosingBracket(char typedClosing) {
        if (codeArea.getSelection().getLength() > 0) {
            return false;
        }

        int caretPos = codeArea.getCaretPosition();
        String text = codeArea.getText();
        if (caretPos < text.length() && text.charAt(caretPos) == typedClosing) {
            codeArea.moveTo(caretPos + 1);
            return true;
        }

        return false;
    }

    private static boolean isOpeningBracket(char c) {
        return c == '(' || c == '{' || c == '[';
    }

    private static boolean isClosingBracket(char c) {
        return c == ')' || c == '}' || c == ']';
    }

    private static char matchingBracket(char opening) {
        return switch (opening) {
            case '(' -> ')';
            case '{' -> '}';
            case '[' -> ']';
            default -> opening;
        };
    }

    private static String leadingWhitespace(String text) {
        int index = 0;
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
        return text.substring(0, index);
    }

    private static char nextNonWhitespaceChar(String text, int startIndex) {
        for (int i = startIndex; i < text.length(); i++) {
            char current = text.charAt(i);
            if (!Character.isWhitespace(current)) {
                return current;
            }
        }
        return '\0';
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("PAREN") != null ? "paren"
                            : matcher.group("BRACE") != null ? "brace"
                                    : matcher.group("BRACKET") != null ? "bracket"
                                            : matcher.group("SEMICOLON") != null ? "semicolon"
                                                    : matcher.group("STRING") != null ? "string"
                                                            : matcher.group("CHAR") != null ? "string"
                                                                    : matcher.group("COMMENT") != null ? "comment"
                                                                            : matcher.group("NUMBER") != null ? "number"
                                                                                    : matcher
                                                                                            .group("ANNOTATION") != null
                                                                                                    ? "annotation"
                                                                                                    : null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        return spansBuilder.create();
    }

    public String getCode() {
        return codeArea.getText();
    }

    public void setCode(String code) {
        codeArea.replaceText(code);
        codeArea.moveTo(0);
    }

    public void clear() {
        codeArea.clear();
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    public void setEditable(boolean editable) {
        codeArea.setEditable(editable);
    }

    public void setAutoIndentEnabled(boolean autoIndentEnabled) {
        this.autoIndentEnabled = autoIndentEnabled;
    }

    public void setAutoBracketPairingEnabled(boolean autoBracketPairingEnabled) {
        this.autoBracketPairingEnabled = autoBracketPairingEnabled;
    }
}
