package com.crowdin.util;

import com.crowdin.api.model.LanguageBuilder;
import com.crowdin.client.languages.model.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PlaceholderUtilTest {

    private static final String STANDARD_PATTERN = "/values-%android_code%/%original_file_name%";

    @ParameterizedTest
    @MethodSource
    public void testReplaceFileDependentPlaceholders(String pattern, String sourcePath, String expected) {
        String result = PlaceholderUtil.replaceFilePlaceholders(pattern, sourcePath);
        assertEquals(String.format("pattern: %s, sourcePath: %s, expected: %s", pattern, sourcePath, expected), expected, result);
    }

    public static Stream<Arguments> testReplaceFileDependentPlaceholders() {
        return Stream.of(
            arguments(STANDARD_PATTERN, "values/strings.xml", "/values-%android_code%/strings.xml")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testReplaceLanguageDependentPlaceholders(String pattern, Language language, String expected) {
        String result = PlaceholderUtil.replaceLanguagePlaceholders(pattern, language, LanguageMapping.fromServerLanguageMapping(new HashMap<>()));
        assertEquals(String.format("pattern: %s, language: %s", pattern, language), expected, result);
    }

    public static Stream<Arguments> testReplaceLanguageDependentPlaceholders() {
        return Stream.of(
            arguments(STANDARD_PATTERN, LanguageBuilder.UKR.build(), "/values-uk-rUA/%original_file_name%")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testBuildTranslationPatterns(String translationPattern, List<Language> projLanguages, LanguageMapping languageMapping, String relativeSourcePath, Map<Language, String> expected) {
        Map<Language, String> actual = PlaceholderUtil.buildTranslationPatterns(relativeSourcePath, translationPattern, projLanguages, languageMapping);
        assertEquals(String.format("pattern: %s, actual: %s, expected: %s", translationPattern, actual, expected), expected, actual);
    }

    public static Stream<Arguments> testBuildTranslationPatterns() {
        return Stream.of(arguments(STANDARD_PATTERN, Arrays.asList(LanguageBuilder.UKR.build()), LanguageMapping.fromServerLanguageMapping(new HashMap<>()), "values/strings.xml", Collections.singletonMap(LanguageBuilder.UKR.build(), "/values-uk-rUA/strings.xml")));
    }

    @ParameterizedTest
    @MethodSource
    public void testFormatSourcePatternForRegex(String toFormat, String expected) {
         String actual = PlaceholderUtil.formatSourcePatternForRegex(toFormat);
        assertEquals(String.format("pattern: %s, actual: %s, expected: %s", toFormat, actual, expected), expected, actual);
    }

    public static Stream<Arguments> testFormatSourcePatternForRegex() {
        return Stream.of(arguments(STANDARD_PATTERN, "/values-%android_code%/[^/]+"));
    }
}
