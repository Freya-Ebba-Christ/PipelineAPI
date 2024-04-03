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

public class PipelineDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create message queues for the pipeline
        MessageQueue inputQueue = new MessageQueue();
        MessageQueue intermediateQueue = new MessageQueue(); // For components that might act in between
        MessageQueue outputQueue = new MessageQueue();

        // Initialize components with specified concurrency settings where applicable
        DataSourceComponent dataSource = new DataSourceComponent(outputQueue, 2); // Example: 2 threads for data generation
        DataProcessorComponent dataProcessor = new DataProcessorComponent(outputQueue, intermediateQueue, 4); // Example: 4 threads for processing
        DataSinkComponent dataSink = new DataSinkComponent(intermediateQueue, 2); // Example: 2 threads for sinking data

        // Optionally, include a TeeComponent or other specialized components as needed
        // MessageQueue copyQueue = new MessageQueue(); // If using a TeeComponent to duplicate data
        // TeeComponent teeComponent = new TeeComponent(intermediateQueue, outputQueue, copyQueue, 2); // Example

        // Start the data source component to simulate data generation and sending
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println("DataSource sending message: " + i);
                    dataSource.fetchData(); // Ensure fetchData generates appropriate data frames
                    Thread.sleep(1000); // Simulate time delay between data generation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        // Wait for a while to let the pipeline process data
        Thread.sleep(10000); // Adjust as needed based on the pipeline's expected processing time

        // Properly shutdown components and executors to clean up resources
        System.out.println("Initiating shutdown...");
        dataSource.shutdown();
        dataProcessor.shutdown();
        dataSink.shutdown();
        // teeComponent.shutdown(); // If using a TeeComponent or similar

        System.out.println("Demo completed.");
    }
}
