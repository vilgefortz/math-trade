package trade.math.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trade.math.form.NewTradeItemForm;
import trade.math.model.TradeItem;
import trade.math.repository.TradeItemRepository;
import trade.math.repository.TradeUserRepository;
import trade.math.wrappers.PageWrapper;
import trade.math.wrappers.TradeItemPageWrapper;

import java.util.List;

/**
 * Created by karol on 17.02.16.
 */
@Service
@Transactional
public class SimpleTradeItemService implements TradeItemService {

    private final TradeItemRepository tradeItemRepository;

    private final BggIdToTitleService bggIdToTitleService;

    private final TradeUserRepository tradeUserRepository;

    @Autowired
    public SimpleTradeItemService(TradeItemRepository tradeItemRepository,
                                  BggIdToTitleService bggIdToTitleService,
                                  TradeUserRepository tradeUserRepository) {
        this.tradeItemRepository = tradeItemRepository;
        this.bggIdToTitleService = bggIdToTitleService;
        this.tradeUserRepository = tradeUserRepository;
    }

    @Override
    public TradeItem save(NewTradeItemForm tradeItemForm, String username) {
        TradeItem tradeItem = new TradeItem();

        tradeItem.setOwner(tradeUserRepository.findOneByUsername(username));

        tradeItem.setBggId(tradeItemForm.getBggId());
        tradeItem.setDescription(tradeItemForm.getDescription());
        tradeItem.setForTrade(false);
        tradeItem.setTitle(bggIdToTitleService.getBoardGameTitle(tradeItemForm.getBggId()));
        return tradeItemRepository.save(tradeItem);
    }

    @Override
    public List<TradeItem> findAll() {
        return tradeItemRepository.findAll();
    }

    @Override
    public void deleteAll() {
        tradeItemRepository.deleteAll();
    }

    @Override
    public boolean deleteById(Long itemId) {
        try {
            tradeItemRepository.delete(itemId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public TradeItem findById(Long itemId) {
        return tradeItemRepository.findOne(itemId);
    }

    @Override
    public PageWrapper<TradeItem> findAll(Pageable pageable) {
        return new TradeItemPageWrapper(tradeItemRepository.findAll(pageable).getContent(), pageable, tradeItemRepository.count());
    }

}
