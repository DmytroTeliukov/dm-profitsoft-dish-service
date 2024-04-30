package com.dteliukov.profitsoftlab2.parsers;

import java.io.InputStream;
import java.util.List;

public interface ReaderParser<T> {
    List<T> read(InputStream inputStream);
}
