/*
 * Copyright (C) 2024 Freya Ebba Christ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package mediaframework.mediaframework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataProcessorComponent extends PipelineComponent {
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorComponent.class);

    // Add a constructor parameter for the number of threads to be used by the executor service
    public DataProcessorComponent(MessageQueue inputQueue, MessageQueue outputQueue, int numberOfThreads) {
        super(inputQueue, outputQueue, numberOfThreads); // Pass numberOfThreads to the superclass constructor
    }

    @Override
    protected CustomDataFrame processData(CustomDataFrame data) {
        logger.info("Processing data: {}", data.getData());

        // Instead of creating a new CustomDataFrame here, let's consider modifying the existing one
        // for efficiency and to maintain any other metadata or structure it may have.

        // Check if the "Data" column exists and process it.
        if (data.getData().containsKey("Data")) {
            List<Object> originalDataColumn = data.getData().get("Data");

            // Apply processing to each element in the "Data" column.
            List<Object> processedDataColumn = originalDataColumn.stream()
                .map(obj -> obj.toString() + "-processed")
                .collect(Collectors.toList());

            // Update the "Data" column with processed data or add a new column, depending on your needs.
            // Here, updating the existing "Data" column:
            data.getData().put("Data", processedDataColumn); // Or use addColumn for a new column
        } else {
            // Log or handle the case where the expected "Data" column is missing
            logger.warn("Expected 'Data' column not found in CustomDataFrame");
        }

        // Return the modified (or potentially unchanged) data frame
        return data;
    }
}



