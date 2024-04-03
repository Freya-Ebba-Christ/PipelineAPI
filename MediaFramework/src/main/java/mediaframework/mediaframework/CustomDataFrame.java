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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomDataFrame {
    private Map<String, List<Object>> data;
    private double number;
    private LocalDateTime timestamp;
    private final long sequenceNumber; // Unique sequence number for each instance

    public CustomDataFrame() {
        this.data = new HashMap<>();
        this.number = 0.0;
        // Utilize ClockAndSequenceGenerator for timestamp and sequence number
        this.timestamp = ClockAndSequenceGenerator.getCurrentTimestamp(); // Get current timestamp
        this.sequenceNumber = ClockAndSequenceGenerator.getNextSequenceNumber(); // Get next sequence number
    }


    // Method to add a column with initial data
    public void addColumn(String columnName, List<Object> columnData) {
        data.put(columnName, new ArrayList<>(columnData));
    }

    // Method to add an empty column
    public void addColumn(String columnName) {
        data.put(columnName, new ArrayList<>());
    }

    // Method to add a row of data; rowData keys must match existing columns
    public void addRow(Map<String, Object> rowData) {
        rowData.forEach((key, value) -> {
            List<Object> column = data.computeIfAbsent(key, k -> new ArrayList<>());
            column.add(value);
        });
    }

    // Method to retrieve data from a specific cell
    public Object getCellData(String columnName, int rowIndex) {
        List<Object> column = data.get(columnName);
        if (column == null || rowIndex < 0 || rowIndex >= column.size()) {
            return null; // Or throw an exception depending on your error handling strategy
        }
        return column.get(rowIndex);
    }

    // Method to update data in a specific cell
    public void updateCell(String columnName, int rowIndex, Object newData) {
        List<Object> column = data.get(columnName);
        if (column != null && rowIndex >= 0 && rowIndex < column.size()) {
            column.set(rowIndex, newData);
        } else {
            throw new IllegalArgumentException("Invalid column name or row index");
        }
    }

    // Getters and Setters
    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public Map<String, List<Object>> getData() {
        return new HashMap<>(data);
    }

    // Add any other methods as needed
}

