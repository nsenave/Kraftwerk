package fr.insee.kraftwerk.core.sequence;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

import fr.insee.kraftwerk.core.inputs.ModeInputs;
import fr.insee.kraftwerk.core.metadata.MetadataUtils;
import fr.insee.kraftwerk.core.metadata.VariablesMap;
import fr.insee.kraftwerk.core.outputs.OutputFiles;
import fr.insee.kraftwerk.core.utils.FileUtils;
import fr.insee.kraftwerk.core.vtl.VtlBindings;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WriterSequence {

	public void writeOutputFiles(Path inDirectory, VtlBindings vtlBindings, Map<String, ModeInputs> modeInputsMap, String multimodeDatasetNames,Map<String, VariablesMap> metadataVariables) {
		Path outDirectory = FileUtils.transformToOut(inDirectory);
		/* Step 4.1 : write csv output tables */
		OutputFiles outputFiles = new OutputFiles(outDirectory, vtlBindings,  new ArrayList<>(modeInputsMap.keySet()), multimodeDatasetNames);
		outputFiles.writeOutputCsvTables();

		/* Step 4.2 : write scripts to import csv tables in several languages */
		outputFiles.writeImportScripts(metadataVariables);
	}
}
