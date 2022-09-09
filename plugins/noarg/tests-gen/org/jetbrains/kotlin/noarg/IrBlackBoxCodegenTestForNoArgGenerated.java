/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.noarg;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/noarg/testData/box")
@TestDataPath("$PROJECT_ROOT")
public class IrBlackBoxCodegenTestForNoArgGenerated extends AbstractIrBlackBoxCodegenTestForNoArg {
    @Test
    public void testAllFilesPresentInBox() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/noarg/testData/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("initializers.kt")
    public void testInitializers() throws Exception {
        runTest("plugins/noarg/testData/box/initializers.kt");
    }

    @Test
    @TestMetadata("initializersWithoutInvokeInitializers.kt")
    public void testInitializersWithoutInvokeInitializers() throws Exception {
        runTest("plugins/noarg/testData/box/initializersWithoutInvokeInitializers.kt");
    }

    @Test
    @TestMetadata("kt18245.kt")
    public void testKt18245() throws Exception {
        runTest("plugins/noarg/testData/box/kt18245.kt");
    }

    @Test
    @TestMetadata("kt18667.kt")
    public void testKt18667() throws Exception {
        runTest("plugins/noarg/testData/box/kt18667.kt");
    }

    @Test
    @TestMetadata("kt18668.kt")
    public void testKt18668() throws Exception {
        runTest("plugins/noarg/testData/box/kt18668.kt");
    }

    @Test
    @TestMetadata("localClassInInitiailzer.kt")
    public void testLocalClassInInitiailzer() throws Exception {
        runTest("plugins/noarg/testData/box/localClassInInitiailzer.kt");
    }

    @Test
    @TestMetadata("nestedClass.kt")
    public void testNestedClass() throws Exception {
        runTest("plugins/noarg/testData/box/nestedClass.kt");
    }

    @Test
    @TestMetadata("sealedClassWithExistingNoargCtor.kt")
    public void testSealedClassWithExistingNoargCtor() throws Exception {
        runTest("plugins/noarg/testData/box/sealedClassWithExistingNoargCtor.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() throws Exception {
        runTest("plugins/noarg/testData/box/simple.kt");
    }

    @Test
    @TestMetadata("subclass.kt")
    public void testSubclass() throws Exception {
        runTest("plugins/noarg/testData/box/subclass.kt");
    }
}
