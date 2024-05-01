package com.dteliukov.profitsoftlab2.parsers;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Interface for writing data to a ByteArrayInputStream.
 *
 * @param <T> The type of data to write.
 */
public interface WriterParser<T> {
    /**
     * Writes data from a list of objects of type T to a ByteArrayInputStream.
     *
     * @param data The list of objects to write.
     * @return A ByteArrayInputStream containing the written data.
     */
    ByteArrayInputStream write(List<T> data);
}
