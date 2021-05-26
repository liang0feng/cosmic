package kded.webservice.demo;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author rd_feng_liang
 * @date 2020/11/6
 */
@WebService
public interface IHelloWorld {

    public Person sayHello (@WebParam(name = "yourname", targetNamespace = "http://impl.demo.kws.kded")String yourname);
}
