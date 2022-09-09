/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.kapt3.test.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.junit.jupiter.api.Tag;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/kapt3/kapt3-compiler/testData/kotlinRunner")
@TestDataPath("$PROJECT_ROOT")
@Tag("IgnoreJDK11")
public class KotlinKaptContextTestGenerated extends AbstractKotlinKaptContextTest {
    @Test
    public void testAllFilesPresentInKotlinRunner() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/kapt3/kapt3-compiler/testData/kotlinRunner"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
    }

    @Test
    @TestMetadata("DefaultParameterValues.kt")
    public void testDefaultParameterValues() throws Exception {
        runTest("plugins/kapt3/kapt3-compiler/testData/kotlinRunner/DefaultParameterValues.kt");
    }

    @Test
    @TestMetadata("ErrorLocationMapping.kt")
    public void testErrorLocationMapping() throws Exception {
        runTest("plugins/kapt3/kapt3-compiler/testData/kotlinRunner/ErrorLocationMapping.kt");
    }

    @Test
    @TestMetadata("NestedClasses.kt")
    public void testNestedClasses() throws Exception {
        runTest("plugins/kapt3/kapt3-compiler/testData/kotlinRunner/NestedClasses.kt");
    }

    @Test
    @TestMetadata("Overloads.kt")
    public void testOverloads() throws Exception {
        runTest("plugins/kapt3/kapt3-compiler/testData/kotlinRunner/Overloads.kt");
    }

    @Test
    @TestMetadata("Simple.kt")
    public void testSimple() throws Exception {
        runTest("plugins/kapt3/kapt3-compiler/testData/kotlinRunner/Simple.kt");
    }
}
