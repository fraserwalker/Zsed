package sk.zsdis.fuse.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import sk.zsdis.fuse.model.Request;

import java.util.Optional;

public class JdbcProcessor implements Processor {

    private static void chainWhere(StringBuilder sb) {
        final String AND = " AND";
        if (sb.length() > 0) sb.append(AND);
    }

    private static void eicParameter(Request request, StringBuilder sb) {
        final String WHERE_EIC_PRE_LIST = " eic IN ('";
        final String WHERE_EIC_POST_LIST = "')";
        if (request.getEic() != null && !request.getEic().isEmpty()) {
            chainWhere(sb);
            sb.append(WHERE_EIC_PRE_LIST)
                    .append(String.join(("','"), request.getEic()))
                    .append(WHERE_EIC_POST_LIST);
        }
    }

    private static void validFromParameter(Request request, StringBuilder sb) {
        final String WHERE_VALID_FROM_PART_1 = " ((pomut.platnost_do IS NULL AND '";
        final String WHERE_VALID_FROM_PART_2 = "' >= pomut.platnost_od) OR ('";
        final String WHERE_VALID_FROM_PART_3 = "' BETWEEN pomut.platnost_od AND pomut.platnost_do))";
        Optional.ofNullable(request.getValidFrom()).ifPresent(validFrom -> {
            chainWhere(sb);
            sb.append(WHERE_VALID_FROM_PART_1)
                    .append(validFrom)
                    .append(WHERE_VALID_FROM_PART_2)
                    .append(validFrom)
                    .append(WHERE_VALID_FROM_PART_3);
        });
    }

    private static void traderEicParameter(Request request, StringBuilder sb) {
        final String WHERE_TRADER_EIC_PRE_PARAM = " pomut.ucastnik_trhu_eic = '";
        final String WHERE_TRADER_EIC_POST_PARAM = "'";
        Optional.ofNullable(request.getTraderEIC()).ifPresent(traderEIC -> {
            chainWhere(sb);
            sb.append(WHERE_TRADER_EIC_PRE_PARAM)
                    .append(traderEIC)
                    .append(WHERE_TRADER_EIC_POST_PARAM);
        });
    }

    public void process(Exchange exchange) throws Exception {
        Request request = (Request) exchange.getMessage().getBody();
        String SELECT = "SELECT om.eic, om.kod_hdo_public ";
        String FROM = "FROM odberne_miesto om " +
                "LEFT JOIN priradenie_odberneho_miesta_ucastnikovi_trhu pomut " +
                "ON om.eic = pomut.odberne_miesto_eic";
        String WHERE = " WHERE";
        StringBuilder querySelect = new StringBuilder(SELECT).append(FROM);
        StringBuilder queryWhere = new StringBuilder();
        eicParameter(request, queryWhere);
        validFromParameter(request, queryWhere);
        traderEicParameter(request, queryWhere);
        if (queryWhere.length() > 0) querySelect.append(WHERE).append(queryWhere.toString());
        querySelect.append(";");
        exchange.getIn().setBody(querySelect.toString());
    }

}
