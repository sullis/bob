package io.jonasg.bob.test;

import io.jonasg.bob.ValidatableField;
import java.lang.String;

public final class DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder implements GenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder.BuildStep, GenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder {
  private int year;

  private final ValidatableField<String> make = ValidatableField.ofNoneNullableField("make", "GenerateStepBuilderWithSingleMandatoryAnnotatedField");

  private double engineSize;

  private boolean isElectric;

  private float fuelEfficiency;

  public DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder() {
  }

  public DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder year(int year) {
    this.year = year;
    return this;
  }

  public DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder make(String make) {
    this.make.set(make);
    return this;
  }

  public DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder engineSize(
      double engineSize) {
    this.engineSize = engineSize;
    return this;
  }

  public DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder isElectric(
      boolean isElectric) {
    this.isElectric = isElectric;
    return this;
  }

  public DefaultGenerateStepBuilderWithSingleMandatoryAnnotatedFieldBuilder fuelEfficiency(
      float fuelEfficiency) {
    this.fuelEfficiency = fuelEfficiency;
    return this;
  }

  public GenerateStepBuilderWithSingleMandatoryAnnotatedField build() {
    var instance = new GenerateStepBuilderWithSingleMandatoryAnnotatedField();
    instance.setYear(this.year);
    instance.setMake(this.make.orElseThrow());
    instance.setEngineSize(this.engineSize);
    instance.setIsElectric(this.isElectric);
    instance.setFuelEfficiency(this.fuelEfficiency);
    return instance;
  }
}
