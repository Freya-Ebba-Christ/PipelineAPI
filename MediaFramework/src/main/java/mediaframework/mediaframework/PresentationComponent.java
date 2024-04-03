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

public class PresentationComponent extends PipelineComponent {
    private static final Logger logger = LoggerFactory.getLogger(PresentationComponent.class);

    // Constructor updated to include an optional number of threads parameter for potential future use
    public PresentationComponent(MessageQueue inputQueue, int numberOfThreads) {
        // Since this component only receives and presents data, it doesn't need an output queue.
        super(inputQueue, null, numberOfThreads); // Configure with potential concurrency in mind
    }

    // Default constructor for when specific concurrency settings are not necessary
    public PresentationComponent(MessageQueue inputQueue) {
        this(inputQueue, 1); // Assume a single thread for simplicity, as extensive processing is not expected
    }

    @Override
    protected CustomDataFrame processData(CustomDataFrame data) {
        // Log presenting of data. Assuming data.getData() returns a meaningful representation
        logger.info("Presenting data: {}", data.getData());

        // Extract and log specific fields if necessary. Adjust field names as per actual data structure.
        // Example: logger.info("Specific data field: {}", data.getData().get("SomeFieldName"));

        // Since this component is an endpoint in the pipeline and doesn't forward data,
        // simply returning null to indicate no further forwarding is required.
        return null;
    }
}





