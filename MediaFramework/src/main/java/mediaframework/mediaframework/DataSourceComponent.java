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

import java.util.HashMap;
import java.util.Map;

public class DataSourceComponent extends PipelineComponent {

    // Constructor for specifying the output queue and optionally the number of threads
    public DataSourceComponent(MessageQueue outputQueue, int numberOfThreads) {
        // This component only sends data out, hence null for the inputQueue.
        // The numberOfThreads parameter allows configuration of the executor service for any background tasks.
        super(null, outputQueue, numberOfThreads);
    }

    // Default constructor assumes a single-threaded execution for simplicity.
    public DataSourceComponent(MessageQueue outputQueue) {
        this(outputQueue, 1); // Default to single-threaded execution if not specified.
    }

    // Method to fetch or generate data and send it to the output queue
    public void fetchData() {
        // Simulate fetching or generating a row of data
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Data", "Sample data from source");
        rowData.put("MoreData", 123); // Example of adding various types of data

        // Create a new CustomDataFrame
        CustomDataFrame dataFrame = new CustomDataFrame();

        // Since CustomDataFrame now contains a structure for complex data, 
        // consider modifying CustomDataFrame to directly support adding rowData if not already supported
        dataFrame.addRow(rowData);

        // Use the sendToQueue method inherited from PipelineComponent to send the CustomDataFrame
        sendToQueue(dataFrame);
    }
}
