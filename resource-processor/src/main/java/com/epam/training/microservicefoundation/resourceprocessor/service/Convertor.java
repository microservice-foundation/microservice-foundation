package com.epam.training.microservicefoundation.resourceprocessor.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Convertor<O, I> {
    O covert(I input) throws IOException;
}
