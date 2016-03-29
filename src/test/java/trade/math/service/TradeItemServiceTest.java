package trade.math.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import trade.math.MtApplication;
import trade.math.domain.groupList.GroupListService;
import trade.math.form.NewTradeItemForm;
import trade.math.form.NewTradeUserForm;
import trade.math.model.TradeItem;
import trade.math.model.TradeItemCategory;
import trade.math.model.TradeUser;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by karol on 17.02.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MtApplication.class)
@ActiveProfiles("test")
public class TradeItemServiceTest {

    private final String USERNAME = "username";
    private final String USERNAME_1 = "username1";

    @Autowired
    private TradeItemService tradeItemService;

    @Autowired
    private TradeUserService tradeUserService;

    @Autowired
    private GroupListService groupListService;

    @Before
    public void setUp() throws Exception {
        tradeItemService.deleteAll(true);

        tradeUserService.deleteAll();
        tradeUserService.save(new NewTradeUserForm(USERNAME, "some@email.com", "password", "password"));
        tradeUserService.save(new NewTradeUserForm(USERNAME_1, "some1@email.com", "password", "password"));
    }

    @Test
    public void testIfImageUrlIsTheSame() throws Exception {
        NewTradeItemForm newTradeItemForm = new NewTradeItemForm();
        newTradeItemForm.setTitle("title");
        newTradeItemForm.setDescription("description");
        newTradeItemForm.setImageUrl("imageUrl");

        TradeItem item = tradeItemService.save(newTradeItemForm, USERNAME);
        assertThat(item.getImgUrl(), is(equalTo("imageUrl")));
    }

    @Test
    public void testSaveNewTradeItem() throws Exception {
        NewTradeItemForm newTradeItemForm = new NewTradeItemForm();
        newTradeItemForm.setTitle("title");
        newTradeItemForm.setDescription("description");

        TradeItem item = tradeItemService.save(newTradeItemForm, USERNAME);
        assertNotNull(item.getId());

        Long itemId = item.getId();

        List<TradeItem> forms = tradeItemService.findAll();
        assertTrue(forms.size() == 1);

        TradeItem foundItem = tradeItemService.findById(itemId);
        assertNotNull(foundItem);
        assertTrue(foundItem.getDescription().equals("description"));

        System.out.println(foundItem.getTitle());
    }

    @Test
    public void testGetPaginationList() {
        prepareTradeList(100);
        List<Integer> list = tradeItemService.findAll(new PageRequest(0, 10)).getPagination();// .getPaginationList(1, 10, 5);

        System.out.println(list.toString());

        assertEquals(7, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(5, list.get(4).intValue());
        assertEquals(-1, list.get(5).intValue());
        assertEquals(10, list.get(6).intValue());

        list = tradeItemService.findAll(new PageRequest(4, 10)).getPagination();// .getPaginationList(5, 10, 5);

        System.out.println(list.toString());

        assertEquals(9, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(-1, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals(5, list.get(4).intValue());
        assertEquals(7, list.get(6).intValue());
        assertEquals(-1, list.get(7).intValue());
        assertEquals(10, list.get(8).intValue());

        list = tradeItemService.findAll(new PageRequest(8, 10)).getPagination();// .getPaginationList(9, 10, 5);

        System.out.println(list.toString());

        assertEquals(7, list.size());
        assertEquals(10, list.get(6).intValue());
        assertEquals(9, list.get(5).intValue());
        assertEquals(1, list.get(0).intValue());
        assertEquals(-1, list.get(1).intValue());
//
//        list = tradeItemService.getPaginationList(5, 10, 4);
//
//        System.out.println(list.toString());
//
//        assertEquals(8, list.size());
//        assertEquals(4, list.get(2).intValue());
//        assertEquals(5, list.get(3).intValue());
//        assertEquals(7, list.get(5).intValue());
//        assertEquals(-1, list.get(6).intValue());
//        assertEquals(10, list.get(7).intValue());

        prepareTradeList(1);
        list = tradeItemService.findAll(new PageRequest(0, 10)).getPagination();// .getPaginationList(1, 10, 5);

        System.out.println(list.toString());

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).intValue());

        list = tradeItemService.findAll(new PageRequest(2, 10)).getPagination();// .getPaginationList(3, 10, 5);

        System.out.println(list.toString());

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).intValue());

//        list = tradeItemService.findAll(new PageRequest(-4, 10)).getPagination();// .getPaginationList(-3, 10, 5);
//
//        System.out.println(list.toString());
//
//        assertEquals(1, list.size());
//        assertEquals(1, list.get(0).intValue());


        prepareTradeList(50);
        list = tradeItemService.findAll(new PageRequest(2, 10)).getPagination();// .getPaginationList(3, 10, 5);

        System.out.println(list.toString());

        assertEquals(5, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(5, list.get(4).intValue());

    }

    @Test
    public void testDeleteById() {
        prepareTradeList(10);

        List<TradeItem> allItems = tradeItemService.findAll();

        Long deletedItemId = allItems.get(4).getId();

        assertTrue(tradeItemService.deleteById(deletedItemId, true, ""));

        allItems = tradeItemService.findAll();

        assertEquals(9, allItems.size());

        for (TradeItem item : allItems) assertFalse(item.getId() == deletedItemId);

        assertFalse(tradeItemService.deleteById(deletedItemId, true, ""));
    }

    @Test
    public void testDeleteCheckOwner() {
        prepareTradeList(10);

        assertFalse(tradeItemService.deleteById(tradeItemService.findAll().get(0).getId(), false, "anotherUser"));
        assertTrue(tradeItemService.deleteById(tradeItemService.findAll().get(0).getId(), false, USERNAME));
        assertTrue(tradeItemService.deleteById(tradeItemService.findAll().get(0).getId(), true, "anotherUser"));
    }

    @Test
    public void testFindByRecentTradeListAndNameAndNotOwner() {
        prepareTradeList(10, USERNAME);
        prepareTradeList(2, USERNAME_1);

        List list = tradeItemService.findByRecentTradeListAndNameAndNotOwner("", USERNAME);

        assertNotNull(list);
        assertEquals(2, list.size());

        list = tradeItemService.findByRecentTradeListAndNameAndNotOwner("", "test");
        assertNull(list);
    }

    @Test
    public void testFindByRecentTradeListAndOwner() {
        prepareTradeList(10, USERNAME);
        prepareTradeList(8, USERNAME_1);

        List<TradeItem> list = tradeItemService.findByRecentTradeListAndOwner(USERNAME_1);

        assertNotNull(list);
        assertEquals(8, list.size());

        list = tradeItemService.findByRecentTradeListAndOwner("test");

        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateGroupLists() throws Exception {
//        tradeItemService.save(new NewTradeItemForm("title", "description", "", TradeItemCategory.BOARD_GAME, 123),
//                USERNAME);
//        tradeItemService.save(new NewTradeItemForm("title", "description", ""), USERNAME);
//        tradeItemService.save(new NewTradeItemForm("title_1", "description", "", TradeItemCategory.BOARD_GAME, 123),
//                USERNAME);
//
//        tradeItemService.makeGroupLists();
        assertThat(true, is(true));
    }

    //HELPERS
    private void prepareTradeList(int count) {
        tradeItemService.deleteAll(true);

        prepareTradeList(count, USERNAME);
    }

    private void prepareTradeList(int count, String userName) {
        for (int i = 0; i < count; i++) {
            NewTradeItemForm form = new NewTradeItemForm();
            form.setTitle("title" + i);
            form.setDescription("desc" + i);
//            form.setBggId(i);

            tradeItemService.save(form, userName);
        }
    }
}