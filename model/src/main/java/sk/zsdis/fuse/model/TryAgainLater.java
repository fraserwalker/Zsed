package sk.zsdis.fuse.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TryAgainLater")
public class TryAgainLater {
    private static String message = "An error was encountered. Please try again later";

    public static String getMessage() {
        return message;
    }
}
