package io.jonasg.bob.test;

import java.lang.String;

public interface GenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder {
  static GenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder newBuilder() {
    return new DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder();
  }

  BuildStep make(String make);

  interface BuildStep {
    BuildStep fuelEfficiency(float fuelEfficiency);

    BuildStep isElectric(boolean isElectric);

    BuildStep engineSize(double engineSize);

    BuildStep year(int year);

    GenerateStepBuilderWithSingleMandatoryAnnotatedField build();
  }
}
