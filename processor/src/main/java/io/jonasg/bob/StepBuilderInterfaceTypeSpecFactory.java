package io.jonasg.bob;

import static io.jonasg.bob.Strategy.STRICT;
import static io.jonasg.bob.Strategy.STEP_WISE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import io.jonasg.bob.TypeSpecInterfaceBuilder.InterfaceBuilder;
import io.jonasg.bob.definitions.TypeDefinition;

public class StepBuilderInterfaceTypeSpecFactory {

	private final Buildable buildable;

	private final TypeDefinition typeDefinition;

	private final List<BuildableField> buildableFields;

	private final String packageName;

	public StepBuilderInterfaceTypeSpecFactory(TypeDefinition typeDefinition,
			Buildable buildable,
			List<BuildableField> buildableFields,
			String packageName) {
		this.buildable = buildable;
		this.typeDefinition = typeDefinition;
		this.buildableFields = buildableFields;
		this.packageName = packageName;
	}

	final class BuilderDetails {
		private final TypeSpec typeSpec;
		private final Set<TypeName> interfaces;

		BuilderDetails(TypeSpec typeSpec, Set<TypeName> interfaces) {
			this.typeSpec = typeSpec;
			this.interfaces = interfaces;
		}

		public TypeSpec typeSpec() {
			return typeSpec;
		}

		public Set<TypeName> interfaces() {
			return interfaces;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj == null || obj.getClass() != this.getClass())
				return false;
			var that = (BuilderDetails) obj;
			return Objects.equals(this.typeSpec, that.typeSpec) &&
					Objects.equals(this.interfaces, that.interfaces);
		}

		@Override
		public int hashCode() {
			return Objects.hash(typeSpec, interfaces);
		}

		@Override
		public String toString() {
			return "BuilderDetails[" +
					"typeSpec=" + typeSpec + ", " +
					"interfaces=" + interfaces + ']';
		}

	}

	BuilderDetails typeSpec(String builderImplName) {
		Set<TypeName> interfaces = new HashSet<>();
		String builderInterfaceName = String.format("%sBuilder", this.typeDefinition.typeName());
		Builder stepBuilderBuilder = TypeSpec.interfaceBuilder(builderInterfaceName)
				.addModifiers(Modifier.PUBLIC);
		interfaces.add(ClassName.get(this.packageName, builderInterfaceName));

		// add static newBuilder method
		stepBuilderBuilder.addMethod(MethodSpec.methodBuilder(builderStaticFactoryMethodName())
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(ClassName.get(this.packageName, builderInterfaceName))
				.addStatement("return new $L()", builderImplName)
				.build());

		List<BuildableField> reversedBuildableFields = reverseList(this.buildableFields);

		// add final BuildStep containing all none mandatory fields
		InterfaceBuilder buildStepInterfaceBuilder = TypeSpecInterfaceBuilder.anInterface("BuildStep");
		reversedBuildableFields.stream()
				.filter(this::notExcluded)
				.filter(field -> (!field.isConstructorArgument()) && !field.isMandatory())
				.forEach(field -> buildStepInterfaceBuilder.addMethod(MethodSpec.methodBuilder(setterName(field.name()))
						.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
						.returns(ClassName.get("", "BuildStep"))
						.addParameter(TypeName.get(field.type()), field.name())
						.build()));
		// add terminal build method
		buildStepInterfaceBuilder.addMethod(MethodSpec.methodBuilder("build")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.returns(ClassName.get(this.typeDefinition.packageName(), this.typeDefinition.typeName()))
				.build());
		buildStepInterfaceBuilder.build();
		TypeSpec buildStep = buildStepInterfaceBuilder.build();
		stepBuilderBuilder.addType(buildStep);
		interfaces.add(ClassName.get("", builderInterfaceName + "." + "BuildStep"));

		// add each mandatory field as a separate interface
		// skipping the last element because that should be defined as a method within
		// the interface itself
		AtomicReference<TypeSpec> nextStep = new AtomicReference<>(buildStep);
		List<BuildableField> mandatoryFields = reversedBuildableFields
				.stream()
				.filter(field -> (field.isConstructorArgument() && isEnforcedConstructorPolicy())
						|| field.isMandatory())
				.collect(Collectors.toList());
		mandatoryFields
				.subList(0, mandatoryFields.size() - 1)
				.stream()
				.filter(this::notExcluded)
				.map(field -> {
					String name = String.format("%sStep", capitalize(field.name()));
					interfaces.add(ClassName.get("", builderInterfaceName + "." + name));
					return TypeSpecInterfaceBuilder.functionalInterface(name)
							.methodName(setterName(field.name()))
							.addArgument(TypeName.get(field.type()), field.name())
							.returns(ClassName.get("", nextStep.get().name))
							.build();
				})
				.peek(nextStep::set)
				.forEach(stepBuilderBuilder::addType);

		// the initial field to be built
		BuildableField buildableField = mandatoryFields
				.get(mandatoryFields.size() - 1);
		stepBuilderBuilder.addMethod(MethodSpec.methodBuilder(setterName(buildableField.name()))
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.addParameter(TypeName.get(buildableField.type()), buildableField.name())
				.returns(ClassName.get("", nextStep.get().name))
				.build());
		return new BuilderDetails(stepBuilderBuilder.build(), interfaces);
	}

	private String builderStaticFactoryMethodName() {
		return this.buildable.factoryName().isEmpty() ? "newBuilder" : this.buildable.factoryName();
	}

	private String capitalize(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	private boolean isEnforcedConstructorPolicy() {
		return Arrays.stream(this.buildable.strategy())
				.anyMatch(s -> List.of(STRICT, STEP_WISE).contains(s));
	}

	private boolean notExcluded(BuildableField field) {
		return !Arrays.asList(buildable.excludeFields()).contains(field.name());
	}

	private <T> List<T> reverseList(List<T> originalList) {
		List<T> reversedList = new ArrayList<>();
		for (int i = originalList.size() - 1; i >= 0; i--) {
			reversedList.add(originalList.get(i));
		}
		return reversedList;
	}

	protected String setterName(String name) {
		if (buildable.setterPrefix().isEmpty()) {
			return name;
		}
		return Formatter.format("$setterPrefix$name", buildable.setterPrefix(),
				name.substring(0, 1).toUpperCase() + name.substring(1));
	}
}
