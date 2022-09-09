/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.test;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateJsTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("js/js.translator/testData/sourcemap")
@TestDataPath("$PROJECT_ROOT")
public class SourceMapGenerationSmokeTestGenerated extends AbstractSourceMapGenerationSmokeTest {
    @Test
    public void testAllFilesPresentInSourcemap() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("js/js.translator/testData/sourcemap"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS, true);
    }

    @Test
    @TestMetadata("binaryOperation.kt")
    public void testBinaryOperation() throws Exception {
        runTest("js/js.translator/testData/sourcemap/binaryOperation.kt");
    }

    @Test
    @TestMetadata("emptyIfInsideInlineLambda.kt")
    public void testEmptyIfInsideInlineLambda() throws Exception {
        runTest("js/js.translator/testData/sourcemap/emptyIfInsideInlineLambda.kt");
    }

    @Test
    @TestMetadata("expressionBody.kt")
    public void testExpressionBody() throws Exception {
        runTest("js/js.translator/testData/sourcemap/expressionBody.kt");
    }

    @Test
    @TestMetadata("methodCallInMethod.kt")
    public void testMethodCallInMethod() throws Exception {
        runTest("js/js.translator/testData/sourcemap/methodCallInMethod.kt");
    }
}
