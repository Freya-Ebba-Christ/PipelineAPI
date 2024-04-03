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

public class DataSinkComponent extends PipelineComponent {
    private static final Logger logger = LoggerFactory.getLogger(DataSinkComponent.class);

    // Constructor now includes an additional parameter for specifying the number of threads
    public DataSinkComponent(MessageQueue inputQueue, int numberOfThreads) {
        // Since this component only receives and processes data, it doesn't need an output queue.
        // Pass the numberOfThreads parameter to the superclass constructor to configure the executor service accordingly.
        super(inputQueue, null, numberOfThreads);
    }

    @Override
    protected CustomDataFrame processData(CustomDataFrame data) {
        // Log the reception of data using the data's toString method for a summary
        logger.info("DataSink received data frame with sequence number: {}", data.getSequenceNumber());

        // Implement any necessary logic to handle or store the data here
        // For example, if storing data in a database or writing to a file, this is where that logic would go.
        // Note: If this operation is IO-intensive and could benefit from parallelization, consider 
        // implementing batching or parallel writing strategies within this method.

        // Since this component is an endpoint in the pipeline and doesn't forward data, return null.
        return null;
    }

    // You may want to add additional methods specific to the data handling/storing functionality of this component,
    // such as methods to flush data to storage, handle batching, or manage connections to external systems.
}
