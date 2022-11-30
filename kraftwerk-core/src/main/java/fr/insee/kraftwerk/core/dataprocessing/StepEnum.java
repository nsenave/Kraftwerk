package fr.insee.kraftwerk.core.dataprocessing;

import lombok.Getter;

public enum StepEnum {
    BUILD_BINDINGS(1,"BUILD_BINDINGS"),
    UNIMODAL_PROCESSING(2,"UNIMODAL_PROCESSING"),
    MULTIMODAL_PROCESSING(3,"MULTIMODAL_PROCESSING");

	@Getter
    private int stepNumber;

	@Getter
    private String stepLabel;

    StepEnum(int stepNumber, String stepLabel){
        this.stepNumber=stepNumber;
        this.stepLabel=stepLabel;
    }
}
