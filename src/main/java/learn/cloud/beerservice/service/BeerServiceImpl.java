package learn.cloud.beerservice.service;

import learn.cloud.beerservice.domain.Beer;
import learn.cloud.beerservice.exceptions.NotFoundException;
import learn.cloud.beerservice.repository.BeerRepository;
import learn.cloud.beerservice.web.mapper.BeerMapper;
import learn.cloud.beerservice.web.model.BeerDto;
import learn.cloud.beerservice.web.model.BeerPagedList;
import learn.cloud.beerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setName(beerDto.getName());
        beer.setStyle(beerDto.getStyle());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());
    }

    @Override
    public BeerDto getById(UUID beerId) {
        return beerMapper.beerToBeerDto(
                beerRepository.findById(beerId)
                        .orElseThrow(NotFoundException::new));
    }

    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest) {
        Page<Beer> beerPage;
        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle))
            beerPage = beerRepository.findAllByNameAndStyle(beerName, beerStyle, pageRequest);
        else if (!StringUtils.isEmpty(beerName))
            beerPage = beerRepository.findAllByName(beerName, pageRequest);
        else if (!StringUtils.isEmpty(beerStyle))
            beerPage = beerRepository.findAllByStyle(beerStyle, pageRequest);
        else
            beerPage = beerRepository.findAll(pageRequest);

        return new BeerPagedList(
                beerPage.getContent()
                        .stream()
                        .map(beerMapper::beerToBeerDtoWithInventory)
                        .collect(Collectors.toList()),
                PageRequest.of(beerPage.getPageable().getPageNumber(),
                        beerPage.getPageable().getPageSize()),
                beerPage.getTotalPages()
        );
    }
}
