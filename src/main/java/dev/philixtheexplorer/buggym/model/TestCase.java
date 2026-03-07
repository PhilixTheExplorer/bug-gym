package dev.philixtheexplorer.buggym.model;

/**
 * Represents a single test case with input and expected output.
 */
public record TestCase(
        String input,
        String expectedOutput) {
    /**
     * Normalizes the expected output for comparison.
     * Trims whitespace and normalizes line endings.
     */
    public String normalizedExpectedOutput() {
        return normalizeOutput(expectedOutput);
    }

    /**
     * Checks if the actual output matches the expected output.
     */
    public boolean matches(String actualOutput) {
        return normalizeOutput(actualOutput).equals(normalizedExpectedOutput());
    }

    /**
     * Normalizes output by trimming and standardizing line endings.
     */
    public static String normalizeOutput(String output) {
        if (output == null)
            return "";
        String normalized = output
                .replace("\r\n", "\n")
                .replace("\r", "\n");

        String[] lines = normalized.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].stripTrailing();
        }

        return String.join("\n", lines).trim();
    }

    @Override
    public String toString() {
        return "TestCase{input='%s', expected='%s'}".formatted(
                input.replace("\n", "\\n"),
                expectedOutput.replace("\n", "\\n"));
    }
}
