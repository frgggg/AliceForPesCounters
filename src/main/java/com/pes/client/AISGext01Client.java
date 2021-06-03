package com.pes.client;

import com.pes.payload.AISGorod.request.AISReq;
import com.pes.payload.AISGorod.responce.AISAccountCountersRes;
import com.pes.payload.AISGorod.responce.AISSetCounterIndicationRes;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

//url = "http://spb19-aisgext01.pes.spb.ru:5445"
@FeignClient(
        value = "AISGext01",
        url = "http://91.231.141.138:5445/GUPTEKSN/AIS.SN.CRMServrice.aspx"
)
public interface AISGext01Client {
    @PostMapping(value = "/GUPTEKSN/AIS.SN.CRMServrice.aspx", produces = "application/json")
    @Headers("Content-Type: application/json")
    @ResponseBody
    ResponseEntity<AISAccountCountersRes> accountCounters(@RequestBody AISReq res);

    @PostMapping(value = "/GUPTEKSN/AIS.SN.CRMServrice.aspx", produces = "application/json")
    @Headers("Content-Type: application/json")
    @ResponseBody
    ResponseEntity<AISSetCounterIndicationRes> setCounterIndication(@RequestBody AISReq res);
}
