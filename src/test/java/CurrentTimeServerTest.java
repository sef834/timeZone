import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CurrentTimeServerTest {

    @Test
    void getCurrentTime() {
        String cT = CurrentTimeServer.getCurrentTime(null);
        Assertions.assertNotNull(cT);
        System.out.println(cT);
    }

    @Test
    void getCurrentTimeWithCustomZone(){
        String cT = CurrentTimeServer.getCurrentTime("America/Los_Angeles");
        Assertions.assertNotNull(cT);
        System.out.println(cT);
    }

    @Test
    void getCurrentTimeWithInvalidZone(){
        String cT = CurrentTimeServer.getCurrentTime("AmeriAngeles");
        Assertions.assertNotNull(cT);
        System.out.println(cT);
    }

    @Test
    void handleRequest() {
        String r = CurrentTimeServer.handleRequest("PUT /zone=utc HTTP/1.1");
        Assertions.assertEquals(true, r.contains("400"));
    }

    @Test
    void isValidRequest(){
        boolean result = CurrentTimeServer.isGETRequest("GET / HTTP/1.1");
        boolean resultWithParams = CurrentTimeServer.isGETRequest("GET /?zone=utc HTTP/1.1");

        Assertions.assertTrue(result);
        Assertions.assertTrue(resultWithParams);
    }
}