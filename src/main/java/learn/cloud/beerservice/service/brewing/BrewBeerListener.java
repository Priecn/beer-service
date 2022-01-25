package learn.cloud.beerservice.service.brewing;

import learn.cloud.beerservice.config.JmsConfig;
import learn.cloud.beerservice.domain.Beer;
import learn.cloud.common.events.BrewBeerEvent;
import learn.cloud.common.events.NewInventoryEvent;
import learn.cloud.beerservice.repository.BeerRepository;
import learn.cloud.common.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event) {
        BeerDto beerDto = event.getBeerDto();
        Beer beer = beerRepository.getById(beerDto.getId());
        beerDto.setQuantityOnHand(beer.getQuantityToBrew());
        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);
        log.debug("Brewed beer: " + beer.getMinOnHand() + ", QOH: " + beerDto.getQuantityOnHand());
        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}
