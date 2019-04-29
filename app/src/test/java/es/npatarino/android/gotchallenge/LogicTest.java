package es.npatarino.android.gotchallenge;

import org.junit.Test;

import java.util.List;

import es.npatarino.android.gotchallenge.services.JsonObject;
import es.npatarino.android.gotchallenge.services.WebServices;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LogicTest {
    @Test
    public void getData_isCorrect() throws Exception {
        List<JsonObject> result = WebServices.getInstace().getData();
        assertEquals(34, result.size());
    }


}