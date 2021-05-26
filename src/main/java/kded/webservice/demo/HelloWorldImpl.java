package kded.webservice.demo;


/**
 * @author rd_feng_liang
 * @date 2020/11/6
 */
public class HelloWorldImpl implements IHelloWorld {


    @Override
    public Person sayHello(String yourname) {

        Person person = new Person();
        person.setName("hello");
        person.setSex("world");
        return person;
    }
}
