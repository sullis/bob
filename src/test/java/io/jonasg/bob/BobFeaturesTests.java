package io.jonasg.bob;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import io.toolisticon.cute.JavaFileObjectUtils;

public class BobFeaturesTests {

	@Test
	void failWhenMultipleConstructorAreAnnotatedWithBuildableConstructor() {
		Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/failing-compilation/MultipleBuildableConstructorAnnotationsPresent/MultipleBuildableConstructorAnnotationsPresent.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationFails()
				.andThat()
				.compilerMessage()
				.ofKindError()
				.contains("Only one constructor can be annotated with @BuildableConstructor")
				.executeTest();
	}

	@Test
	void allConstructorParamsAreBuildable() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/AllConstructorParamsAreBuildable/AllConstructorParamsAreBuildable.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.builder.AllConstructorParamsAreBuildableBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/AllConstructorParamsAreBuildable/Expected_AllConstructorParamsAreBuildable.java"))
				.executeTest();
	}

	@Test
	void allConstructorParamsAreBuildableIfHavingMatchingField() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/AllConstructorParamsAreBuildableIfHavingMatchingField/AllConstructorParamsAreBuildableIfHavingMatchingField.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.builder.AllConstructorParamsAreBuildableIfHavingMatchingFieldBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/AllConstructorParamsAreBuildableIfHavingMatchingField/Expected_AllConstructorParamsAreBuildableIfHavingMatchingField.java"))
				.executeTest();
	}

	@Test
	void defaultValuesForParamsWithNoneMatchingField() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/AllConstructorParamsAreBuildableIfHavingMatchingField/AllConstructorParamsAreBuildableIfHavingMatchingField.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.builder.AllConstructorParamsAreBuildableIfHavingMatchingFieldBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/AllConstructorParamsAreBuildableIfHavingMatchingField/Expected_AllConstructorParamsAreBuildableIfHavingMatchingField.java"))
				.executeTest();
	}

	@Test
	void overrideDefaultPackage() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/OverrideDefaultPackage/OverrideDefaultPackage.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.other.OverrideDefaultPackageBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/OverrideDefaultPackage/Expected_OverrideDefaultPackage.java"))
				.executeTest();
	}

	@Test
	void setterWithCustomPrefix() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/SetterWithCustomPrefix/SetterWithCustomPrefix.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.builder.SetterWithCustomPrefixBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/SetterWithCustomPrefix/Expected_SetterWithCustomPrefix.java"))
				.executeTest();
	}

	@Test
	void useConstructorAnnotatedWithBuildableConstructor() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/UseConstructorAnnotatedWithBuildableConstructor/UseConstructorAnnotatedWithBuildableConstructor.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.builder.UseConstructorAnnotatedWithBuildableConstructorBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/UseConstructorAnnotatedWithBuildableConstructor/Expected_UseConstructorAnnotatedWithBuildableConstructor.java"))
				.executeTest();
	}

	@Test
	void useConstructorWithTheGreatestNumberOfParameters() {
				Cute.blackBoxTest()
				.given()
				.processors(List.of(BuildableProcessor.class))
				.andSourceFiles("/tests/successful-compilation/UseConstructorWithTheGreatestNumberOfParameters/UseConstructorWithTheGreatestNumberOfParameters.java")
				.whenCompiled()
				.thenExpectThat()
				.compilationSucceeds()
				.andThat()
				.generatedSourceFile("io.jonasg.bob.test.builder.UseConstructorWithTheGreatestNumberOfParametersBuilder")
				.matches(
						CuteApi.ExpectedFileObjectMatcherKind.BINARY,
						JavaFileObjectUtils.readFromResource("/tests/successful-compilation/useConstructorWithTheGreatestNumberOfParameters/Expected_UseConstructorWithTheGreatestNumberOfParameters.java"))
				.executeTest();
	}

}