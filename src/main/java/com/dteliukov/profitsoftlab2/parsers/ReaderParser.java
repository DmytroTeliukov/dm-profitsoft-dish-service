package com.dteliukov.profitsoftlab2.parsers;

import java.io.InputStream;
import java.util.List;


/**
 * Interface for parsing data from an InputStream.
 *
 * @param <T> The type of data to parse.
 */
public interface ReaderParser<T> {
    /*
    Reads data from an InputStream and parses it into a list of objects of type T.
    @param inputStream The InputStream from which to read the data.
    @return A list of objects of type T parsed from the input stream.
    */
    List<T> read(InputStream inputStream);
}
