package sk.zsdis.fuse.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class Request {

    private List<String> eic = new ArrayList<String>();
    private Date validFrom;
    private String traderEIC;

    @JsonProperty("EIC")
    @XmlElementWrapper(name = "EICS")
    @XmlElement(name = "EIC")
    public List<String> getEic() {
        return eic;
    }

    public void setEic(List<String> eic) {
        this.eic = eic;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public String getTraderEIC() {
        return traderEIC;
    }

    public void setTraderEIC(String traderEIC) {
        this.traderEIC = traderEIC;
    }

}
