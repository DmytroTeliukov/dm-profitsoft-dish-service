package com.dteliukov.profitsoftlab2.parsers;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface WriterParser<T> {
    ByteArrayInputStream write(List<T> data);
}
