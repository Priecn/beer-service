package learn.cloud.beerservice.service;

import learn.cloud.beerservice.domain.Beer;
import learn.cloud.beerservice.exceptions.NotFoundException;
import learn.cloud.beerservice.repository.BeerRepository;
import learn.cloud.beerservice.web.mapper.BeerMapper;
import learn.cloud.beerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public UUID saveNewBeer(BeerDto beerDto) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDto)).getId();
    }

    @Override
    public UUID updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setName(beerDto.getName());
        beer.setStyle(beerDto.getStyle());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerRepository.save(beer).getId();
    }

    @Override
    public BeerDto getById(UUID beerId) {
        return beerMapper.beerToBeerDto(
                beerRepository.findById(beerId)
                        .orElseThrow(NotFoundException::new));
    }
}
