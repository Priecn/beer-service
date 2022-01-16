package learn.cloud.beerservice.service;

import learn.cloud.beerservice.web.model.BeerDto;

import java.util.UUID;


public interface BeerService {
    UUID saveNewBeer(BeerDto beerDto);

    UUID updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getById(UUID beerId);
}
