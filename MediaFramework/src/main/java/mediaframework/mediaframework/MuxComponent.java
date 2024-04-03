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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MuxComponent extends PipelineComponent {
    private PriorityQueue<CustomDataFrame> queue;
    private final ExecutorService processingExecutor;

    // Constructor now includes an additional parameter for specifying the number of threads
    public MuxComponent(MessageQueue inputQueue, MessageQueue outputQueue, int numberOfThreads) {
        super(inputQueue, outputQueue, numberOfThreads); // Adjusted to match the new PipelineComponent constructor
        this.queue = new PriorityQueue<>(Comparator.comparingLong(frame -> {
            ZonedDateTime zdt = frame.getTimestamp().atZone(ZoneId.systemDefault());
            return zdt.toEpochSecond();
        }));
        // Initialize the processingExecutor with the specified number of threads for concurrent frame processing
        this.processingExecutor = Executors.newFixedThreadPool(numberOfThreads);
        startProcessingFrames();
    }

    private synchronized void addFrame(CustomDataFrame frame) {
        queue.offer(frame);
    }

    private synchronized CustomDataFrame getNextFrame() {
        return queue.poll();
    }

    private void startProcessingFrames() {
        processingExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                CustomDataFrame frame = getNextFrame();
                if (frame != null) {
                    logger.info("MuxComponent processing frame with SeqNum: {}", frame.getSequenceNumber());
                    // The following ensures that the processing and forwarding are also handled concurrently
                    processingExecutor.submit(() -> {
                        sendToQueue(frame); // Forwarding the frame to the next component
                    });
                }
            }
        });
    }

    @Override
    public void shutdown() {
        super.shutdown();
        // Ensure both executors are properly shut down
        if (!processingExecutor.isShutdown()) {
            processingExecutor.shutdownNow();
        }
    }
}
