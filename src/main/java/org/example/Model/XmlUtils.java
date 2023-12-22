package org.example.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Optional;

public class XmlUtils {
    private XmlUtils() {
    }

    public static <T> Optional<T> parse(String path, Class<T> clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object obj = jaxbUnmarshaller.unmarshal(new File(path));
            T res = (T) obj;
            return Optional.of(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
