package learn.cloud.beerservice.service;

import learn.cloud.common.model.BeerDto;
import learn.cloud.beerservice.web.model.BeerPagedList;
import learn.cloud.common.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;


public interface BeerService {
    UUID saveNewBeer(BeerDto beerDto);

    void updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto getByUPC(String upc, Boolean showInventoryOnHand);

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);
}
