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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PipelineComponent {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final MessageQueue inputQueue;
    protected final MessageQueue outputQueue;
    private ExecutorService executorService; // Now variable, to support configuration
    protected DataProcessor dataProcessor = new DefaultDataProcessor();

    // Constructor now includes a parameter for the number of threads
    public PipelineComponent(MessageQueue inputQueue, MessageQueue outputQueue, int numberOfThreads) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        // Configure the executor service based on the provided number of threads
        this.executorService = Executors.newFixedThreadPool(numberOfThreads);
        startListening();
    }

    
    protected void startListening() {
        CompletableFuture.runAsync(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    CustomDataFrame frame = (CustomDataFrame) inputQueue.receiveMessage();
                    if (frame != null) {
                        CompletableFuture.supplyAsync(() -> processData(frame), executorService)
                                .thenAcceptAsync(processedData -> {
                                    if (outputQueue != null && processedData != null) {
                                        sendToQueue(processedData);
                                    }
                                }, executorService);
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    logger.error("An error occurred during message processing: {}", e.getMessage(), e);
                }
            }
        }, executorService);
    }

    // This method remains unchanged, but now it's executed within the executor's task
    protected CustomDataFrame processData(CustomDataFrame data) {
        return dataProcessor.processData(data);
    }

    public void sendToQueue(CustomDataFrame message) {
        CompletableFuture.runAsync(() -> {
            if (outputQueue != null) {
                outputQueue.sendMessage(message);
            }
        }, executorService).exceptionally(e -> {
            logger.error("Failed to send message to queue: {}", e.getMessage());
            return null;
        });
    }

    public void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    // Setter for the data processor allows for dynamic changes in processing logic
    public void setDataProcessor(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }
}