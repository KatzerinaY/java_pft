package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class GroupCreationTests extends TestBase {

    @Test
    public void testGroupCreation()  {
        app.goTo().groupPage();
        List<GroupData> before = app.group().list();
        GroupData group = new GroupData().withName("test2");
        app.group().create(group);
        List<GroupData> after = app.group().list();
        Assert.assertEquals(after.size(), before.size() + 1);

        Comparator<? super GroupData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());
        group.setId(after.stream().max(byId).get().getId());
        before.add(group);

        //        Assert.assertEquals(new HashSet<Object>(before), new HashSet<Object>(after));
        before.sort(byId);
        after.sort(byId);
        Assert.assertEquals(before, after);

    }

}
