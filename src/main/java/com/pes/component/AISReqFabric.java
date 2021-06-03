package com.pes.component;

import com.pes.payload.AISGorod.request.AISReq;
import com.pes.payload.AISGorod.request.AISReqParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AISReqFabric {
    public static final String TYPE_REQUEST = "typeRequest";

    public static final String ACCOUNT_PARAM_NAME = "account";
    public static final String BASE_ID_PARAM_NAME = "baseId";
    //TODO - set in properties!
    //TODO: rel
    //public static final String BASE_ID_PARAM_VAL = "1";
    //TODO: test
    public static final String BASE_ID_PARAM_VAL = "60";


    public static final String ACCOUNT_COUNTER_TYPE_REQUEST = TYPE_REQUEST;
    public static final String ACCOUNT_COUNTER_METOD_REQUEST = "AccountCounters";
    public static final String ACCOUNT_COUNTER_ACCOUNT_PARAM_NAME = ACCOUNT_PARAM_NAME;
    public static final String ACCOUNT_COUNTER_BASE_ID_PARAM_NAME = BASE_ID_PARAM_NAME;
    public static final String ACCOUNT_COUNTER_BASE_ID_PARAM_VAL = BASE_ID_PARAM_VAL;
    public static final String ACCOUNT_COUNTER_NEED_COMMUNAL_PARAM_NAME = "needCommunal";
    public static final String ACCOUNT_COUNTER_NEED_COMMUNAL_PARAM_VAL = null;
    public static final String ACCOUNT_COUNTER_SYSTEM_NAME_PARAM_NAME = "systemName";
    public static final String ACCOUNT_COUNTER_SYSTEM_NAME_VAL = "";

    public static AISReq accountCountersReq(String account) {
        List<AISReqParam> params = new ArrayList<>();
        params.add(new AISReqParam(ACCOUNT_COUNTER_ACCOUNT_PARAM_NAME, account));
        params.add(new AISReqParam(ACCOUNT_COUNTER_BASE_ID_PARAM_NAME, ACCOUNT_COUNTER_BASE_ID_PARAM_VAL));
        params.add(new AISReqParam(ACCOUNT_COUNTER_NEED_COMMUNAL_PARAM_NAME, ACCOUNT_COUNTER_NEED_COMMUNAL_PARAM_VAL));
        params.add(new AISReqParam(ACCOUNT_COUNTER_SYSTEM_NAME_PARAM_NAME, ACCOUNT_COUNTER_SYSTEM_NAME_VAL));
        return new AISReq(
                ACCOUNT_COUNTER_TYPE_REQUEST, ACCOUNT_COUNTER_METOD_REQUEST,
                params
        );
    }

    public static final String SET_COUNTER_INDICATION_TYPE_REQUEST = TYPE_REQUEST;
    public static final String SET_COUNTER_INDICATION_METOD_REQUEST = "SetCounterIndication";
    public static final String SET_COUNTER_INDICATION_ACCOUNT_PARAM_NAME = ACCOUNT_PARAM_NAME;
    public static final String SET_COUNTER_INDICATION_BASE_ID_PARAM_NAME = BASE_ID_PARAM_NAME;
    public static final String SET_COUNTER_INDICATION_BASE_ID_PARAM_VAL = BASE_ID_PARAM_VAL;
    public static final String SET_COUNTER_INDICATION_COUNTER_ID_PARAM_NAME = "counterId";
    public static final String SET_COUNTER_INDICATION_COUNTER_NEW_VAL_PARAM_NAME = "val";
    public static final String SET_COUNTER_INDICATION_INSERT_TYPE_ID_PARAM_NAME = "insertTypeId";
    public static final String SET_COUNTER_INDICATION_INSERT_TYPE_ID_PARAM_VAL = null;
    public static final String SET_COUNTER_INDICATION_INDICATION_TYPE_ID_PARAM_NAME = "indicationTypeId";
    public static final String SET_COUNTER_INDICATION_INDICATION_TYPE_ID_PARAM_VAL = null;
    public static final String SET_COUNTER_INDICATION_COUNTER_VAL_DATE_PARAM_NAME = "valDate";
    public static final String SET_COUNTER_INDICATION_COMMENT_PARAM_NAME = "comment";
    public static final String SET_COUNTER_INDICATION_COMMENT_PARAM_VAL = null;

    public static AISReq setCounterReq(String account, Double counterId, String val, LocalDateTime valTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
        String dateStr = valTime.format(formatter);

        List<AISReqParam> params = new ArrayList<>();
        params.add(new AISReqParam(SET_COUNTER_INDICATION_ACCOUNT_PARAM_NAME, account));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_COUNTER_ID_PARAM_NAME, counterId.toString()));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_COUNTER_NEW_VAL_PARAM_NAME, val));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_COUNTER_VAL_DATE_PARAM_NAME, dateStr));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_BASE_ID_PARAM_NAME, SET_COUNTER_INDICATION_BASE_ID_PARAM_VAL));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_INSERT_TYPE_ID_PARAM_NAME, SET_COUNTER_INDICATION_INSERT_TYPE_ID_PARAM_VAL));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_INDICATION_TYPE_ID_PARAM_NAME, SET_COUNTER_INDICATION_INDICATION_TYPE_ID_PARAM_VAL));
        params.add(new AISReqParam(SET_COUNTER_INDICATION_COMMENT_PARAM_NAME, SET_COUNTER_INDICATION_COMMENT_PARAM_VAL));

        return new AISReq(
                SET_COUNTER_INDICATION_TYPE_REQUEST, SET_COUNTER_INDICATION_METOD_REQUEST,
                params
        );
    }

}
