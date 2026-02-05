package dev.philixtheexplorer.buggym.service;

import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.model.TestCase;
import dev.philixtheexplorer.buggym.model.TestResult;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for compiling and running user code against test cases.
 */
public class CodeRunner {

    private static final long TIMEOUT_MS = 5000; // 5 second timeout per test
    private static final String CLASS_NAME = "Solution";

    private final ExecutorService executor;

    public CodeRunner() {
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "CodeRunner");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Compiles and runs the user's code against all test cases.
     */
    public RunResult runTests(String code, List<TestCase> testCases) {
        long startTime = System.currentTimeMillis();

        // Compile the code
        CompilationResult compilation = compileCode(code);
        if (!compilation.success()) {
            return RunResult.compilationFailure(compilation.errorMessage());
        }

        // Run each test case
        List<TestResult> results = new ArrayList<>();
        for (TestCase testCase : testCases) {
            TestResult result = runSingleTest(compilation.compiledClass(), testCase);
            results.add(result);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        return RunResult.success(results, totalTime);
    }

    /**
     * Compiles and runs the user's code main method without checking output.
     */
    public RunResult runMain(String code) {
        long startTime = System.currentTimeMillis();

        // Compile the code
        CompilationResult compilation = compileCode(code);
        if (!compilation.success()) {
            return RunResult.compilationFailure(compilation.errorMessage());
        }

        TestCase dummyCase = new TestCase("Run main()", "N/A");

        Future<TestResult> future = executor.submit(() -> {
            long tStart = System.currentTimeMillis();
            InputStream originalIn = System.in;
            PrintStream originalOut = System.out;
            try {
                System.setIn(new ByteArrayInputStream(new byte[0]));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);
                System.setOut(printStream);

                try {
                    var mainMethod = compilation.compiledClass().getMethod("main", String[].class);
                    mainMethod.invoke(null, (Object) new String[0]);
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    return TestResult.error(dummyCase, "Runtime error: " + cause.getMessage());
                }

                String output = outputStream.toString(StandardCharsets.UTF_8);
                return TestResult.success(dummyCase, output, System.currentTimeMillis() - tStart);

            } finally {
                System.setIn(originalIn);
                System.setOut(originalOut);
            }
        });

        try {
            TestResult result = future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
            return RunResult.success(List.of(result), System.currentTimeMillis() - startTime);
        } catch (TimeoutException e) {
            future.cancel(true);
            return RunResult.success(
                    List.of(TestResult.error(dummyCase, "Timeout: Execution exceeded " + TIMEOUT_MS + "ms")),
                    System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            return RunResult.success(
                    List.of(TestResult.error(dummyCase, "Execution error: " + e.getMessage())),
                    System.currentTimeMillis() - startTime);
        }
    }

    private CompilationResult compileCode(String code) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return new CompilationResult(false, null,
                    "Java compiler not available. Make sure you're running with a JDK, not a JRE.");
        }

        // Ensure the code has the correct class name
        code = ensureClassName(code);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8))) {

            JavaFileObject sourceFile = new StringJavaFileObject(CLASS_NAME, code);

            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    List.of("-proc:none"), // Disable annotation processing
                    null,
                    List.of(sourceFile));

            boolean success = task.call();

            if (!success) {
                StringBuilder errors = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                        errors.append("Line %d: %s%n".formatted(
                                diagnostic.getLineNumber(),
                                diagnostic.getMessage(null)));
                    }
                }
                return new CompilationResult(false, null, errors.toString());
            }

            // Load the compiled class
            InMemoryClassLoader classLoader = new InMemoryClassLoader(fileManager.getClassBytes());
            Class<?> compiledClass = classLoader.loadClass(CLASS_NAME);

            return new CompilationResult(true, compiledClass, null);

        } catch (Exception e) {
            return new CompilationResult(false, null, "Compilation error: " + e.getMessage());
        }
    }

    private String ensureClassName(String code) {
        // Replace any public class declaration with Solution
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            String foundClass = matcher.group(1);
            if (!foundClass.equals(CLASS_NAME)) {
                code = code.replace("class " + foundClass, "class " + CLASS_NAME);
            }
        }

        return code;
    }

    private TestResult runSingleTest(Class<?> compiledClass, TestCase testCase) {
        Future<TestResult> future = executor.submit(() -> {
            long startTime = System.currentTimeMillis();

            // Redirect System.in and System.out
            InputStream originalIn = System.in;
            PrintStream originalOut = System.out;

            try {
                // Set up input
                ByteArrayInputStream inputStream = new ByteArrayInputStream(
                        testCase.input().getBytes(StandardCharsets.UTF_8));
                System.setIn(inputStream);

                // Set up output capture
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);
                System.setOut(printStream);

                // Run the main method
                try {
                    var mainMethod = compiledClass.getMethod("main", String[].class);
                    mainMethod.invoke(null, (Object) new String[0]);
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    return TestResult.error(testCase, "Runtime error: " + cause.getMessage());
                }

                // Get output
                String actualOutput = outputStream.toString(StandardCharsets.UTF_8);
                long executionTime = System.currentTimeMillis() - startTime;

                // Compare output
                boolean passed = testCase.matches(actualOutput);

                if (passed) {
                    return TestResult.success(testCase, actualOutput, executionTime);
                } else {
                    return TestResult.failure(testCase, TestCase.normalizeOutput(actualOutput), executionTime);
                }

            } finally {
                // Restore System streams
                System.setIn(originalIn);
                System.setOut(originalOut);
            }
        });

        try {
            return future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return TestResult.error(testCase, "Timeout: Execution exceeded " + TIMEOUT_MS + "ms");
        } catch (Exception e) {
            return TestResult.error(testCase, "Execution error: " + e.getMessage());
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    // --- Inner classes for in-memory compilation ---

    private record CompilationResult(boolean success, Class<?> compiledClass, String errorMessage) {
    }

    private static class StringJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        StringJavaFileObject(String className, String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    private static class InMemoryClassFileObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InMemoryClassFileObject(String className) {
            super(URI.create("mem:///" + className.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        byte[] getBytes() {
            return outputStream.toByteArray();
        }
    }

    private static class InMemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, InMemoryClassFileObject> classFiles = new HashMap<>();

        InMemoryFileManager(StandardJavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                JavaFileObject.Kind kind, FileObject sibling) {
            InMemoryClassFileObject classFile = new InMemoryClassFileObject(className);
            classFiles.put(className, classFile);
            return classFile;
        }

        Map<String, byte[]> getClassBytes() {
            Map<String, byte[]> result = new HashMap<>();
            for (var entry : classFiles.entrySet()) {
                result.put(entry.getKey(), entry.getValue().getBytes());
            }
            return result;
        }
    }

    private static class InMemoryClassLoader extends ClassLoader {
        private final Map<String, byte[]> classBytes;

        InMemoryClassLoader(Map<String, byte[]> classBytes) {
            super(InMemoryClassLoader.class.getClassLoader());
            this.classBytes = classBytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classBytes.get(name);
            if (bytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
