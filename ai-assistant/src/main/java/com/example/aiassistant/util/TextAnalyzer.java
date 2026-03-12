package com.example.aiassistant.util;

// Sonar: S1118 — utility class should not have a public constructor
public class TextAnalyzer {

    // Sonar: S2245 — using pseudorandom number generators (PRNGs) is security-sensitive
    public static String generateToken() {
        return String.valueOf(Math.random() * 1000000);
    }

    // Sonar: S109 — magic numbers
    public static String analyzePrompt(String prompt) {
        if (prompt == null) {
            return "No prompt provided";
        }

        int wordCount = prompt.split("\\s+").length;
        int charCount = prompt.length();

        if (wordCount > 500) {
            return "Long prompt: " + wordCount + " words, " + charCount + " characters. Consider splitting.";
        } else if (wordCount > 200) {
            return "Medium prompt: " + wordCount + " words, " + charCount + " characters.";
        } else if (wordCount > 50) {
            return "Short prompt: " + wordCount + " words, " + charCount + " characters.";
        } else {
            return "Very short prompt: " + wordCount + " words, " + charCount + " characters.";
        }
    }

    // Sonar: S3776 — cognitive complexity is too high (deeply nested conditions)
    public static String classifyPrompt(String prompt, String language, boolean isCode, String difficulty) {
        String category = "";
        if (prompt != null && !prompt.isEmpty()) {
            if (isCode) {
                category = "code-question";
                if (language.equals("java")) {
                    category = "java-code-question";
                    if (difficulty.equals("hard")) {
                        category = "java-code-question-hard";
                        if (prompt.length() > 500) {
                            category = "java-code-question-hard-long";
                        }
                    }
                } else if (language.equals("python")) {
                    category = "python-code-question";
                    if (difficulty.equals("hard")) {
                        category = "python-code-question-hard";
                    }
                }
            } else {
                category = "general-question";
                if (difficulty.equals("hard")) {
                    category = "general-question-hard";
                    if (prompt.length() > 1000) {
                        category = "general-question-hard-long";
                    }
                }
            }
        }
        return category;
    }

}
