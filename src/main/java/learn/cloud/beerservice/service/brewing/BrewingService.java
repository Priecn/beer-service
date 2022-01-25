package learn.cloud.beerservice.service.brewing;

import learn.cloud.beerservice.config.JmsConfig;
import learn.cloud.beerservice.domain.Beer;
import learn.cloud.common.events.BrewBeerEvent;
import learn.cloud.beerservice.repository.BeerRepository;
import learn.cloud.beerservice.service.inventory.BeerInventoryService;
import learn.cloud.beerservice.web.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {

    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory() {
        List<Beer> beerList = beerRepository.findAll();

        beerList.forEach(beer -> {
            Integer invQOH = beerInventoryService.getOnHandInventory(beer.getId());
            log.debug("Minimum on hand: " + beer.getMinOnHand());
            log.debug("Quantity on hand: " + invQOH);

            if (beer.getMinOnHand() >= invQOH) {
                jmsTemplate.convertAndSend(
                        JmsConfig.BREWING_REQUEST_QUEUE,
                        new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
