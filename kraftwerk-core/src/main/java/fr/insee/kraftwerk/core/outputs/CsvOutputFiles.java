package fr.insee.kraftwerk.core.outputs;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.insee.kraftwerk.core.KraftwerkError;
import fr.insee.kraftwerk.core.metadata.VariablesMap;
import fr.insee.kraftwerk.core.outputs.scripts.RDataTableImportScript;
import fr.insee.kraftwerk.core.outputs.scripts.SASImportScript;
import fr.insee.kraftwerk.core.outputs.scripts.TableScriptInfo;
import fr.insee.kraftwerk.core.utils.TextFileWriter;
import fr.insee.kraftwerk.core.vtl.VtlBindings;

/**
 * Class to manage the writing of CSV output tables.
 */
public class CsvOutputFiles extends OutputFiles {


	/**
	 * When an instance is created, the output folder is created.
	 * 
	 * @param outDirectory Out directory defined in application properties.
	 * @param vtlBindings  Vtl bindings where datasets are stored.
	 * @param userInputs   Used to get the campaign name and to filter intermediate
	 *                     datasets that we don't want to output.
	 */
	public CsvOutputFiles(Path outDirectory, VtlBindings vtlBindings, List<String> modes, String multimodeDatasetNames) {
		super(outDirectory, vtlBindings, modes, multimodeDatasetNames);
	}


	/**
	 * Method to write CSV output tables from datasets that are in the bindings.
	 */
	@Override
	public void writeOutputTables(Map<String,VariablesMap> metadataVariables) {
		for (String datasetName : getDatasetToCreate()) {
			File outputFile = getOutputFolder().resolve(outputFileName(datasetName)).toFile();
			if (outputFile.exists()) {
				CsvTableWriter.updateCsvTable(getVtlBindings().getDataset(datasetName),
						getOutputFolder().resolve(outputFileName(datasetName)),metadataVariables,datasetName);
			} else {
				CsvTableWriter.writeCsvTable(getVtlBindings().getDataset(datasetName),
						getOutputFolder().resolve(outputFileName(datasetName)),metadataVariables,datasetName);
			}
		}
	}

	@Override
	public void writeImportScripts(Map<String, VariablesMap> metadataVariables, List<KraftwerkError> errors) {
		// Assemble required info to write scripts
		List<TableScriptInfo> tableScriptInfoList = new ArrayList<>();
		for (String datasetName : getDatasetToCreate()) {
			TableScriptInfo tableScriptInfo = new TableScriptInfo(datasetName, outputFileName(datasetName),
					getVtlBindings().getDataset(datasetName).getDataStructure(), metadataVariables);
			tableScriptInfoList.add(tableScriptInfo);
		}
		// Write scripts
		TextFileWriter.writeFile(getOutputFolder().resolve("import_with_data_table.R"),
				new RDataTableImportScript(tableScriptInfoList).generateScript());
		TextFileWriter.writeFile(getOutputFolder().resolve("import.sas"),
				new SASImportScript(tableScriptInfoList,errors).generateScript());
	}

	/**
	 * Return the name of the file to be written from the dataset name.
	 */
	@Override
	public String outputFileName(String datasetName) {
		return getOutputFolder().getFileName() + "_" + datasetName + ".csv";
	}

}
