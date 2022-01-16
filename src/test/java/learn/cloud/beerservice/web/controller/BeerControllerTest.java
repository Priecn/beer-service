package learn.cloud.beerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import learn.cloud.beerservice.bootstrap.BeerLoader;
import learn.cloud.beerservice.web.model.BeerDto;
import learn.cloud.beerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;


@WebMvcTest
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getBeerById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.status().isOk()));

    }

    @Test
    void createNewBeer() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerToString = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerToString))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void updateBeer() throws Exception {

        BeerDto beerDto = getValidBeerDto();
        String beerToString = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerToString))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    BeerDto getValidBeerDto() {
        return BeerDto.builder()
                .name("My Beer")
                .style(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }

    @Test
    void updateBeerInvalid() throws Exception {

        BeerDto beerDto = BeerDto.builder().build();
        String beerToString = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerToString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}