package sk.zsdis.fuse.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {

    private String eic;
    private String kodHdoPublic;

    public String getEic() {
        return eic;
    }

    public void setEic(String eic) {
        this.eic = eic;
    }

    public String getKodHdoPublic() {
        return kodHdoPublic;
    }

    public void setKodHdoPublic(String kodHdoPublic) {
        this.kodHdoPublic = kodHdoPublic;
    }
}
