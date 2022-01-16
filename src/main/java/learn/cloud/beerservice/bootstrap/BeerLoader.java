package learn.cloud.beerservice.bootstrap;

import learn.cloud.beerservice.domain.Beer;
import learn.cloud.beerservice.repository.BeerRepository;
import learn.cloud.beerservice.web.model.BeerStyleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    public static final Long BEER_1_UPC = 063123420003L;
    public static final Long BEER_2_UPC = 063123430001L;
    public static final Long BEER_3_UPC = 063123430003L;


    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) {
        loadBeerList();
    }

    private void loadBeerList() {
        if (beerRepository.count() == 0) {
            beerRepository.save(Beer.builder()
                            .name("Mango bobs")
                            .style(BeerStyleEnum.IPA)
                            .minOnHand(12)
                            .quantityToBrew(200)
                            .upc(BEER_1_UPC)
                            .price(new BigDecimal("12.99"))
                            .build());

            beerRepository.save(Beer.builder()
                    .name("Galaxy Cat")
                    .style(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .price(new BigDecimal("11.59"))
                    .build());

            beerRepository.save(Beer.builder()
                    .name("Pinball Porter")
                    .style(BeerStyleEnum.PALE_ALE)
                    .minOnHand(15)
                    .quantityToBrew(100)
                    .upc(BEER_3_UPC)
                    .price(new BigDecimal("15.3"))
                    .build());
        }

    }
}
