package com.tuniondata.jtserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuniondata.Application;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author yinheli
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public String toJSON(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public <T> T parseJSON(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

}
