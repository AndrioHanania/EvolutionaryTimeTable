package JAVAFXUI;

import generated.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class JavaFxUIUtils {
    public static ETTDescriptor deserializeFrom(InputStream in)  throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(ETTDescriptor.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (ETTDescriptor)u.unmarshal(in);
    }
}
