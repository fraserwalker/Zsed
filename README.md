# Západoslovenská distribučná, a. s. Red Hat Fuse Demonstration Project
#### _Demo projects for engagement 2019-11-20 to 2019-11-29 showing various implementations of Red Hat Fuse 7.4 for OpenShift deployment_
## Modules
* **java-restJson-jdbc**
    * _Code (Java) based Rest Service accepting and responding with JSON body to Postgres Database_
* **java-restJsonXml-jdbc**
    * _Code (Java) Rest Service accepting and responding with JSON or XML body to Postgres Database_
* **java-soap11-jdbc**
    * _Code (Java) Soap 1.1 Service to Postgres Database_
* **java-soap12-jdbc**
    * _Code (Java) Soap 1.2 Service to Postgres Database_
## Requirements
* A service should return data from a database (Postgres) using following select statement:

      SELECT om.eic, om.kod_hdo_public
      FROM odberne_miesto om
      INNER JOIN priradenie_odberneho_miesta_ucastnikovi_trhu pomut ON om.eic = pomut.odberne_miesto_eic
      WHERE eic IN (Parameter_EIC1,Parameter_EIC2,...)
      AND (
       (pomut.platnost_do IS NULL AND Parameter_ValidFrom, >= pomut.platnost_od)
       OR
       (Parameter_ValidFrom BETWEEN pomut.platnost_od AND pomut.platnost_do)
      )
      AND pomut.ucastnik_trhu_eic = Parameter_TraderEIC;
* Parameters should be provided in a payload of request message

      Parameter_EIC1, Parameter_EIC2,... = EIC
      Parameter_ValidFrom = ValidFrom
      Parameter_TraderEIC = TraderEIC
      
    * Example JSON request:
    
          {
           "TestRequest": {
           "EIC": [
            "string"
           ],
           "ValidFrom": Date,
           "TraderEIC": "string",  
           }
          }