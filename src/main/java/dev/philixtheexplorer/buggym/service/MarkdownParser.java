package dev.philixtheexplorer.buggym.service;

import dev.philixtheexplorer.buggym.model.TestCase;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for parsing Markdown content and extracting question metadata.
 */
public class MarkdownParser {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownParser() {
        List<Extension> extensions = List.of(TablesExtension.create());
        this.parser = Parser.builder()
                .extensions(extensions)
                .build();
        this.renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();
    }

    /**
     * Converts Markdown content to HTML for rendering in WebView.
     */
    public String toHtml(String markdown, String baseImagePath) {
        return toHtml(markdown, baseImagePath, true);
    }

    /**
     * Converts Markdown content to HTML for rendering in WebView.
     */
    public String toHtml(String markdown, String baseImagePath, boolean darkMode) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }

        Node document = parser.parse(markdown);
        String bodyHtml = renderer.render(document);

        // Fix relative image paths
        if (baseImagePath != null) {
            bodyHtml = bodyHtml.replaceAll(
                    "src=\"(?!http|file)([^\"]+)\"",
                    "src=\"" + baseImagePath + "/$1\"");
        }

        return wrapInHtmlDocument(bodyHtml, darkMode);
    }

    /**
     * Extracts the question title from markdown content.
     * Expected format: ## Question X: Title or ## Title
     */
    public String extractTitle(String markdown) {
        if (markdown == null)
            return "Untitled";

        Pattern pattern = Pattern.compile("^##\\s+(?:Question\\s*\\d+[:\\s-]*)?(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdown);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Untitled";
    }

    /**
     * Extracts test cases from the markdown table.
     * Expected format: | Input | Result | or | Input | Output |
     */
    public List<TestCase> extractTestCases(String markdown) {
        List<TestCase> testCases = new ArrayList<>();

        if (markdown == null)
            return testCases;

        // Normalize line endings to ensure regex works on all platforms
        markdown = markdown.replace("\r\n", "\n").replace("\r", "\n");

        // Find the table section - look for markdown tables
        // Handle bold/italic formatting: **Input**, *Input*, etc.
        Pattern tablePattern = Pattern.compile(
                "\\|\\s*(?:\\*\\*|\\*)?Input(?:\\*\\*|\\*)?\\s*\\|\\s*(?:\\*\\*|\\*)?(?:Result|Output|Expected)(?:\\*\\*|\\*)?\\s*\\|.*?\\n"
                        +
                        "\\|[-:\\s|]+\\|\\n" +
                        "((?:\\|[^\\n]+\\|\\n?)+)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher tableMatcher = tablePattern.matcher(markdown);

        if (tableMatcher.find()) {
            String tableRows = tableMatcher.group(1);
            parseTableRows(tableRows, testCases);
        } else {
            // Try alternative format without header detection
            parseAlternativeFormat(markdown, testCases);
        }

        return testCases;
    }

    private void parseTableRows(String tableRows, List<TestCase> testCases) {
        String[] lines = tableRows.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("|--") || line.startsWith("| --")) {
                continue;
            }

            // Parse: | input | expected |
            // Use limit -1 to preserve trailing empty strings
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 3) {
                String input = cleanTableCell(parts[1]);
                String expected = cleanTableCell(parts[2]);

                if (!input.isEmpty() || !expected.isEmpty()) {
                    testCases.add(new TestCase(input, expected));
                }
            }
        }
    }

    private void parseAlternativeFormat(String markdown, List<TestCase> testCases) {

        // Also try simpler inline format: Input: X → Output: Y
        // Fixed: Escape hyphen properly in character class
        Pattern inlinePattern = Pattern.compile(
                "Input[:\\s]*[`\"]?([^`\"\\n]+)[`\"]?[\\s]*(?:→|->|Output:|Result:)[\\s]*[`\"]?([^`\"\\n]+)[`\"]?",
                Pattern.CASE_INSENSITIVE);

        Matcher inlineMatcher = inlinePattern.matcher(markdown);
        while (inlineMatcher.find()) {
            String input = inlineMatcher.group(1).trim();
            String expected = inlineMatcher.group(2).trim();
            testCases.add(new TestCase(input, expected));
        }
    }

    private String cleanTableCell(String cell) {
        if (cell == null)
            return "";

        String cleaned = cell.trim();

        // Handle code formatting in cells: `value`
        cleaned = cleaned.replaceAll("^`|`$", "");

        // Handle escaped newlines
        cleaned = cleaned.replaceAll("\\s*\\\\n\\s*", "\n");
        cleaned = cleaned.replaceAll("(?i)\\s*<br\\s*/?>\\s*", "\n");

        // Handle escaped markdown characters
        cleaned = cleaned.replace("\\*", "*");
        cleaned = cleaned.replace("\\_", "_");
        cleaned = cleaned.replace("\\[", "[");
        cleaned = cleaned.replace("\\]", "]");
        cleaned = cleaned.replace("\\|", "|");

        // Handle HTML entities
        cleaned = cleaned.replace("&nbsp;", " ");
        cleaned = cleaned.replace("&lt;", "<");
        cleaned = cleaned.replace("&gt;", ">");
        cleaned = cleaned.replace("&amp;", "&");

        return cleaned;
    }

    /**
     * Extracts hint from markdown content.
     * Expected format: ### Hint or **Hint:** section
     */
    public String extractHint(String markdown) {
        if (markdown == null)
            return null;

        Pattern pattern = Pattern.compile(
                "(?:###\\s*Hint|\\*\\*Hint\\*\\*)[:\\s]*([^#]+?)(?=###|$)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(markdown);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    /**
     * Extracts starter code from markdown content.
     * Expected format:
     * 1. ### Starter Code followed by java code block
     * 2. Text mentioning "preloaded answer box" followed by java code block
     */
    public String extractStarterCode(String markdown) {
        if (markdown == null)
            return null;

        // Standard format: ### Starter Code
        Pattern standardPattern = Pattern.compile(
                "###\\s*Starter\\s*Code[:\\s]*```(?:java)?\\s*([^`]+)```",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher standardMatcher = standardPattern.matcher(markdown);
        if (standardMatcher.find()) {
            return standardMatcher.group(1).trim();
        }

        // Alternative format: "preloaded answer box" context
        Pattern alternativePattern = Pattern.compile(
                "(?:preloaded answer box|use as a template)[^`]*```(?:java)?\\s*([^`]+)```",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher alternativeMatcher = alternativePattern.matcher(markdown);
        if (alternativeMatcher.find()) {
            return alternativeMatcher.group(1).trim();
        }

        return null;
    }

    private String wrapInHtmlDocument(String bodyHtml, boolean darkMode) {
        // Use replace() instead of formatted() since CSS contains percent signs
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        :root {
                            --bg-color: #1e1e1e;
                            --text-color: #d4d4d4;
                            --code-bg: #2d2d2d;
                            --border-color: #404040;
                            --accent-color: #569cd6;
                            --success-color: #4ec9b0;
                            --heading-color: #dcdcaa;
                        }

                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: var(--bg-color);
                            color: var(--text-color);
                            padding: 20px;
                            line-height: 1.6;
                            margin: 0;
                        }

                        h1, h2, h3, h4 {
                            color: var(--heading-color);
                            margin-top: 1.5em;
                            margin-bottom: 0.5em;
                        }

                        h1:first-child, h2:first-child, h3:first-child, h4:first-child {
                            margin-top: 0;
                        }

                        h2 { font-size: 1.5em; border-bottom: 1px solid var(--border-color); padding-bottom: 0.3em; }
                        h3 { font-size: 1.25em; }

                        code {
                            background-color: var(--code-bg);
                            padding: 2px 6px;
                            border-radius: 4px;
                            font-family: 'Consolas', 'Courier New', monospace;
                            font-size: 0.9em;
                        }

                        pre {
                            background-color: var(--code-bg);
                            padding: 16px;
                            border-radius: 8px;
                            overflow-x: auto;
                            border: 1px solid var(--border-color);
                        }

                        pre code {
                            background: none;
                            padding: 0;
                        }

                        table {
                            border-collapse: collapse;
                            width: 100%;
                            margin: 1em 0;
                        }

                        th, td {
                            border: 1px solid var(--border-color);
                            padding: 10px 15px;
                            text-align: left;
                        }

                        th {
                            background-color: var(--code-bg);
                            color: var(--accent-color);
                            font-weight: 600;
                        }

                        tr:nth-child(even) {
                            background-color: rgba(255,255,255,0.03);
                        }

                        img {
                            max-width: 100%;
                            height: auto;
                            border-radius: 8px;
                            margin: 1em 0;
                        }

                        blockquote {
                            border-left: 4px solid var(--accent-color);
                            margin: 1em 0;
                            padding-left: 1em;
                            color: #888;
                        }

                        ul, ol {
                            padding-left: 2em;
                        }

                        li {
                            margin: 0.5em 0;
                        }

                        a {
                            color: var(--accent-color);
                            text-decoration: none;
                        }

                        a:hover {
                            text-decoration: underline;
                        }

                        .light-mode {
                            --bg-color: #ffffff;
                            --text-color: #333333;
                            --code-bg: #f5f5f5;
                            --border-color: #e0e0e0;
                            --accent-color: #0066cc;
                            --heading-color: #333333;
                        }
                    </style>
                </head>
                <body__BODY_CLASS__>
                    __BODY_CONTENT__
                </body>
                </html>
                """;

        return template.replace("__BODY_CLASS__", darkMode ? "" : " class=\"light-mode\"")
                .replace("__BODY_CONTENT__", bodyHtml);
    }
}
