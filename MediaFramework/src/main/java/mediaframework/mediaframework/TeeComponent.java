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

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class TeeComponent extends PipelineComponent {
    private static final Logger logger = LoggerFactory.getLogger(TeeComponent.class);
    
    private final MessageQueue outputQueueForCopy;

    // Adding numberOfThreads parameter to allow configurable concurrency
    public TeeComponent(MessageQueue inputQueue, MessageQueue outputQueue, MessageQueue outputQueueForCopy, int numberOfThreads) {
        super(inputQueue, outputQueue, numberOfThreads); // Configuring the superclass with concurrency support
        this.outputQueueForCopy = outputQueueForCopy;
    }

    @Override
    protected CustomDataFrame processData(CustomDataFrame data) {
        // Log the operation
        logger.info("TeeComponent processing and duplicating data: {}", data.getData());

        // Forward the original data to the next component via the primary output queue
        sendToQueue(data);

        // For copying data, considering deep copy if CustomDataFrame contains mutable objects
        CustomDataFrame dataCopy = deepCopyDataFrame(data);
        
        // Send the copy to the secondary output queue
        sendToQueueForCopy(dataCopy);

        // Since processData effectively forwards without transforming, returning null is appropriate.
        return null;
    }

    private CustomDataFrame deepCopyDataFrame(CustomDataFrame original) {
        // Implement deep copy logic according to the structure of CustomDataFrame
        // This is a placeholder implementation. You'll need to adjust it based on how CustomDataFrame is structured.
        CustomDataFrame copy = new CustomDataFrame();
        for (Map.Entry<String, List<Object>> entry : original.getData().entrySet()) {
            copy.addColumn(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        // Ensure other properties of CustomDataFrame are also copied as needed
        return copy;
    }

    private void sendToQueueForCopy(CustomDataFrame message) {
        if (outputQueueForCopy != null) {
            outputQueueForCopy.sendMessage(message);
        } else {
            logger.error("Output queue for copy is not set.");
        }
    }
}
