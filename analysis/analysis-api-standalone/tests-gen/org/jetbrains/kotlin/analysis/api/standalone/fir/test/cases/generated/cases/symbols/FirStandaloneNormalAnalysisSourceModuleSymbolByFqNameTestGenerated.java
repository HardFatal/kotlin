/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.standalone.fir.test.cases.generated.cases.symbols;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.analysis.api.standalone.fir.test.AnalysisApiFirStandaloneModeTestConfiguratorFactory;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiTestConfiguratorFactoryData;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiTestConfigurator;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.TestModuleKind;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.FrontendKind;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisSessionMode;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiMode;
import org.jetbrains.kotlin.analysis.api.impl.base.test.cases.symbols.AbstractSymbolByFqNameTest;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.analysis.api.GenerateAnalysisApiTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("analysis/analysis-api/testData/symbols/symbolByFqName")
@TestDataPath("$PROJECT_ROOT")
public class FirStandaloneNormalAnalysisSourceModuleSymbolByFqNameTestGenerated extends AbstractSymbolByFqNameTest {
    @NotNull
    @Override
    public AnalysisApiTestConfigurator getConfigurator() {
        return AnalysisApiFirStandaloneModeTestConfiguratorFactory.INSTANCE.createConfigurator(
            new AnalysisApiTestConfiguratorFactoryData(
                FrontendKind.Fir,
                TestModuleKind.Source,
                AnalysisSessionMode.Normal,
                AnalysisApiMode.Standalone
            )
        );
    }

    @Test
    public void testAllFilesPresentInSymbolByFqName() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/analysis-api/testData/symbols/symbolByFqName"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("class.kt")
    public void testClass() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/class.kt");
    }

    @Test
    @TestMetadata("classFromJdk.kt")
    public void testClassFromJdk() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/classFromJdk.kt");
    }

    @Test
    @TestMetadata("enumEntry.kt")
    public void testEnumEntry() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/enumEntry.kt");
    }

    @Test
    @TestMetadata("fileWalkDirectionEnum.kt")
    public void testFileWalkDirectionEnum() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/fileWalkDirectionEnum.kt");
    }

    @Test
    @TestMetadata("iterator.kt")
    public void testIterator() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/iterator.kt");
    }

    @Test
    @TestMetadata("listOf.kt")
    public void testListOf() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/listOf.kt");
    }

    @Test
    @TestMetadata("memberFunction.kt")
    public void testMemberFunction() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/memberFunction.kt");
    }

    @Test
    @TestMetadata("memberFunctionWithOverloads.kt")
    public void testMemberFunctionWithOverloads() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/memberFunctionWithOverloads.kt");
    }

    @Test
    @TestMetadata("nestedClass.kt")
    public void testNestedClass() throws Exception {
        runTest("analysis/analysis-api/testData/symbols/symbolByFqName/nestedClass.kt");
    }

    @Nested
    @TestMetadata("analysis/analysis-api/testData/symbols/symbolByFqName/withTestCompilerPluginEnabled")
    @TestDataPath("$PROJECT_ROOT")
    public class WithTestCompilerPluginEnabled {
        @Test
        public void testAllFilesPresentInWithTestCompilerPluginEnabled() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/analysis-api/testData/symbols/symbolByFqName/withTestCompilerPluginEnabled"), Pattern.compile("^(.+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("myInterfaceSupertype.kt")
        public void testMyInterfaceSupertype() throws Exception {
            runTest("analysis/analysis-api/testData/symbols/symbolByFqName/withTestCompilerPluginEnabled/myInterfaceSupertype.kt");
        }
    }
}
