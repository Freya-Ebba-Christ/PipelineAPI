/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mediaframework.mediaframework;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Freya Ebba Christ
 */

public class ClockAndSequenceGenerator {
    private static final AtomicLong sequence = new AtomicLong(0);

    public static long getNextSequenceNumber() {
        return sequence.incrementAndGet();
    }

    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }
}


