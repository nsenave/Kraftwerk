package fr.insee.kraftwerk.core.outputs;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.insee.kraftwerk.core.Constants;
import fr.insee.kraftwerk.core.TestConstants;
import fr.insee.kraftwerk.core.inputs.UserInputs;
import fr.insee.kraftwerk.core.vtl.VtlBindings;
import fr.insee.vtl.model.Dataset;
import fr.insee.vtl.model.InMemoryDataset;
import fr.insee.vtl.model.Structured;

@TestMethodOrder(OrderAnnotation.class)
class OutputFilesTest {

	private static UserInputs testUserInputs;
	private static OutputFiles outputFiles;

	Dataset fooDataset = new InMemoryDataset(List.of(),
			List.of(new Structured.Component("FOO", String.class, Dataset.Role.IDENTIFIER)));

	@Test
	@Order(1)
	void createInstance() {
		assertDoesNotThrow(() -> {
			//
			testUserInputs = new UserInputs(
					Path.of(TestConstants.UNIT_TESTS_DIRECTORY, "user_inputs/inputs_valid_several_modes.json"),
					Path.of(TestConstants.UNIT_TESTS_DIRECTORY,"user_inputs"));
			//
			VtlBindings vtlBindings = new VtlBindings();
			for (String mode : testUserInputs.getModes()) {
				vtlBindings.put(mode, fooDataset);
			}
			vtlBindings.put(testUserInputs.getMultimodeDatasetName(), fooDataset);
			vtlBindings.put(Constants.ROOT_GROUP_NAME, fooDataset);
			vtlBindings.put("LOOP", fooDataset);
			vtlBindings.put("FROM_USER", fooDataset);
			//
			outputFiles = new CsvOutputFiles(Paths.get(TestConstants.UNIT_TESTS_DUMP), vtlBindings, testUserInputs.getModes(), testUserInputs.getMultimodeDatasetName());
		});
	}

	@Test
	@Order(2)
	void testGetDatasetOutputNames() {
		//
		Set<String> outputDatasetNames = outputFiles.getDatasetToCreate();

		//
		for (String mode : testUserInputs.getModes()) {
			assertFalse(outputDatasetNames.contains(mode));
		}
		assertFalse(outputDatasetNames.contains(testUserInputs.getMultimodeDatasetName()));
		assertTrue(outputDatasetNames.containsAll(Set.of(Constants.ROOT_GROUP_NAME, "LOOP", "FROM_USER")));
	}


	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
}
