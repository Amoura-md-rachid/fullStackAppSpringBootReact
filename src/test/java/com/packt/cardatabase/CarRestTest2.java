package com.packt.cardatabase;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import com.packt.cardatabase.web.CarController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packt.cardatabase.domain.Car;
import com.packt.cardatabase.domain.CarRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
public class CarRestTest2 {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCars() throws Exception {
        Car car1 = new Car("Ford", "Mustang", "Red", "ADF-1121", 2017, 59000);
        Car car2 = new Car("Nissan", "Leaf", "White", "SSJ-3002", 2014, 29000);
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(car1, car2));
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].brand").value("Ford"))
                .andExpect(jsonPath("$[0].model").value("Mustang"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[0].registerNumber").value("ADF-1121"))
                .andExpect(jsonPath("$[0].year").value(2017))
                .andExpect(jsonPath("$[0].price").value(59000))
                .andExpect(jsonPath("$[1].brand").value("Nissan"))
                .andExpect(jsonPath("$[1].model").value("Leaf"))
                .andExpect(jsonPath("$[1].color").value("White"))
                .andExpect(jsonPath("$[1].registerNumber").value("SSJ-3002"))
                .andExpect(jsonPath("$[1].year").value(2014))
                .andExpect(jsonPath("$[1].price").value(29000));
    }
}

