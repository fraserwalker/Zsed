package sk.zsdis.fuse.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Result")
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

    @JsonProperty("kod_hdo_public")
    public void setKodHdoPublic(String kodHdoPublic) {
        this.kodHdoPublic = kodHdoPublic;
    }
}
