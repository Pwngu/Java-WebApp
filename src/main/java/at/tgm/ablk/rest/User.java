package at.tgm.ablk.rest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    private static final Logger LOG = LogManager.getLogger("at.tgm.ablk.rest.User");


    @XmlElement public String email;
    @XmlElement public String password;

    public User() {

        LOG.debug("Initializing");
    }
}
