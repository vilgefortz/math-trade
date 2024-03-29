package trade.math.domain.tradeItem;

import org.hibernate.validator.constraints.NotEmpty;
import trade.math.domain.groupList.GroupListItem;
import trade.math.domain.groupList.ItemGroup;
import trade.math.domain.tradeList.TradeList;
import trade.math.model.TradeItemCategory;
import trade.math.model.TradeUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TRADE_ITEM")
public class TradeItem implements GroupListItem<String> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    private String title;

    @Column(nullable = false)
    private String description;

    private String imgUrl;

    @Column(nullable = false)
    private boolean forTrade;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TradeUser owner;

    @ManyToOne
    @JoinColumn(name = "TRADE_LIST_ID", nullable = true)
    private TradeList tradeList;

    @NotNull
    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private TradeItemCategory category;

    @ManyToOne
    @JoinColumn(name = "ITEM_GROUP_ID")
    private ItemGroup itemGroup;

    @Column(name = "BGG_ID")
    private int bggId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isForTrade() {
        return forTrade;
    }

    public void setForTrade(boolean forTrade) {
        this.forTrade = forTrade;
    }

    public TradeUser getOwner() {
        return owner;
    }

    public void setOwner(TradeUser owner) {
        this.owner = owner;
    }

    public TradeList getTradeList() {
        return tradeList;
    }

    public void setTradeList(TradeList tradeList) {
        this.tradeList = tradeList;
    }

    public TradeItemCategory getCategory() {
        return category;
    }

    public void setCategory(TradeItemCategory category) {
        this.category = category;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public int getBggId() {
        return bggId;
    }

    public void setBggId(int bggId) {
        this.bggId = bggId;
    }

    @Override
    public String getProperty() {
        switch (getCategory()) {
            case NONE:
                return getTitle();
            case BOARD_GAME:
                return String.valueOf(getBggId());
            default:
                throw new IllegalArgumentException("Unknown category.");
        }
    }
}
